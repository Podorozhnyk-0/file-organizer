package ru.podorozhnyk.application;

import ru.podorozhnyk.launcher.Main;
import ru.podorozhnyk.model.FileType;
import ru.podorozhnyk.model.OrganizationResult;
import ru.podorozhnyk.utils.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Organizer {
    private final Path root;
    private final int maxDepth;

    private OrganizationResult.Builder resultBuilder;

    public Organizer(Path rootDir, int maxDepth) {
        FileUtils.requireDirectory(rootDir);
        root = rootDir.toAbsolutePath();
        this.maxDepth = maxDepth;
    }

    public OrganizationResult organizeFiles() {
        resultBuilder = OrganizationResult.Builder.builder();
        try (var paths = Files.walk(root, maxDepth)) {
            paths.filter(Files::isRegularFile).forEach(this::processFile);

        } catch (IOException e ) {
            System.err.println("[ERROR] Error occurred: " + e);
        }
        return resultBuilder.build();
    }

    private void processFile(Path source) {
        Path currentJar;
        try {
            currentJar = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }

        if (source.equals(currentJar)) return;

        FileType type = FileType.from(FileUtils.getFileExt(source));
        Path target = null;
        try {
            Path dest = Files.createDirectories(root.resolve(type.getPath()));
            target = FileUtils.resolveWithoutConflicts(source, dest);

            if (source.getParent().equals(target.getParent())) return;
            Files.move(source, target);
            resultBuilder.ok(source, target);

        } catch (IOException e) {
            if (target != null)
                resultBuilder.skip(source, target);
        }
    }
}
