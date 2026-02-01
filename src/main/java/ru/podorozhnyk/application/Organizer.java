package ru.podorozhnyk.application;

import ru.podorozhnyk.launcher.Main;
import ru.podorozhnyk.model.FileType;
import ru.podorozhnyk.utils.FileUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Organizer {
    private final Path root;
    private final int maxDepth;

    public Organizer(Path rootDir, int maxDepth) {
        FileUtils.requireDirectory(rootDir);
        root = rootDir.toAbsolutePath();
        this.maxDepth = maxDepth;
        IO.println(root);

        organizeFiles();
    }

    private void organizeFiles() {
        try (var paths = Files.walk(root, maxDepth)) {
            paths.filter(Files::isRegularFile).forEach(this::processFile);

        } catch (IOException e ) {
            System.err.println("[ERROR] Error occurred: " + e);
        }
    }

    private void processFile(Path path) {
        Path currentJar;
        try {
            currentJar = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }

        if (path.equals(currentJar)) return;

        FileType type = FileType.from(FileUtils.getFileExt(path));
        try {
            Path dest = Files.createDirectories(root.resolve(type.getPath()));
            Path target = FileUtils.resolveWithoutConflicts(path, dest);

            if (path.getParent().equals(target.getParent())) return;
            IO.println(path + " -> " + target);

            Files.move(path, target);
        } catch (IOException e) {
            System.err.println("[ERROR] Cannot move file " + path.getFileName());
        }
    }
}
