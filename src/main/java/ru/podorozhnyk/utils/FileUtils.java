package ru.podorozhnyk.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class FileUtils {
    private FileUtils() {}

    public static String getFileExt(Path file) {
        String fileName = file.getFileName().toString();
        int lastDot = fileName.lastIndexOf('.');
        return lastDot == -1? "" : fileName.substring(lastDot).toLowerCase();
    }

    public static String getFileNameWithoutExt(Path file) {
        String fileName = file.getFileName().toString();
        int firstDot = fileName.indexOf('.');
        return firstDot == -1? fileName : fileName.substring(0, firstDot);
    }

    public static void requireDirectory(Path path) {
        Objects.requireNonNull(path);
        if (!path.toFile().isDirectory())
            throw new IllegalArgumentException(String.format("\"%s\" must be a directory.", path));
    }

    public static Path resolveWithoutConflicts(Path src, Path dest) {
        Path target = dest.resolve(src.getFileName());
        int counter = 1;
        while (Files.exists(target)) {
            String newFileName = String.format("%s (%d)%s",
                    FileUtils.getFileNameWithoutExt(src),
                    counter++,
                    FileUtils.getFileExt(src));
            target = dest.resolve(newFileName);
        }
        return target;
    }
}
