package ru.podorozhnyk.model;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;

public enum FileType {
    DOCUMENT_TEXT       ("Documents/Text",      Set.of(".txt", ".doc", ".docx", ".pdf", ".odt",
            ".xls", ".log", ".ini", ".xlsx", ".pptx", ".inf", ".cfg", ".config", ".csv", ".rtf", ".epub", ".bat", ".cmd", ".css", ".djvu")),
    DOCUMENT_CODE       ("Documents/Code",      Set.of(".cpp", ".java", ".js", ".html", ".htm", ".json", ".xml", ".py", ".hpp", ".c", ".h")),
    DOCUMENT_ARCHIVE    ("Documents/Archives",  Set.of(".zip", ".7z", ".rar", "tar", ".gz", ".xz", ".bz2", ".zst")),
    DOCUMENT_DISC_IMAGES("Documents/DiscImages",Set.of(".iso")),
    MEDIA_VIDEO         ("Media/Videos",        Set.of(".mp4", ".webm", ".wmv", ".mkv", ".mov", ".avi")),
    MEDIA_AUDIO         ("Media/Audio",         Set.of(".mp3", ".wav", ".ogg", ".m4a", ".flac", ".aac")),
    MEDIA_IMAGE         ("Media/Images",        Set.of(".png", ".jpg", ".ico", ".jpeg", ".gif", ".bmp", ".svg")),
    EXECUTABLE_WIN      ("Executable/Windows",  Set.of(".exe", ".msi", ".dll")),
    EXECUTABLE_LINUX    ("Executable/Linux",    Set.of(".so")),
    EXECUTABLE_MAC_OS   ("Executable/MacOS",    Set.of(".app")),
    EXECUTABLE_ANY      ("Executable/Any",      Set.of(".jar")),
    TORRENT             ("Torrents",            Set.of(".torrent")),
    UNKNOWN             ("Unknown",             Set.of(""));

    private final String path;
    private final Set<String> ext;


    FileType(String path, Set<String> extensions) {
        this.path = path;
        ext = extensions;
    }

    public Path getPath() {
        return Path.of(path);
    }

    public boolean contains(String ext) {
        return this.ext.contains(ext);
    }

    public static FileType from(String ext) {
        return Arrays.stream(FileType.values()).filter(type -> type.contains(ext)).findFirst().orElse(UNKNOWN);
    }
}
