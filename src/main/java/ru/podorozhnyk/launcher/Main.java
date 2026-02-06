package ru.podorozhnyk.launcher;

import ru.podorozhnyk.application.Organizer;
import ru.podorozhnyk.ui.MainWindow;
import ru.podorozhnyk.utils.Arguments;
import ru.podorozhnyk.utils.FileUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private final String NAME = "File Organizer";
    private final String VERSION = "v1.0";
    private final String AUTHOR = "Podorozhnyk-0";

    void main(String[] args) {
        Arguments arguments = Arguments.from(args);
        if (arguments.hasKey("--gui")) {
            IO.println("Work in GUI");
            new MainWindow(NAME, VERSION, AUTHOR);
        } else  {
            Path workingPath = Paths.get("");
            int maxDepth = 1;
            if (arguments.hasKey("--path")) {
                IO.println("Path is " + arguments.getValue("--path"));
                workingPath = Paths.get(arguments.getValue("--path"));
            }
            if (arguments.hasKey("-d")) {
                IO.println("MD is " + arguments.getValue("-d"));
                maxDepth = Integer.parseInt(arguments.getValue("-d"));
                if (maxDepth < 0) maxDepth = Integer.MAX_VALUE;
            }
            var result = new Organizer(workingPath, maxDepth).organizeFiles();
            if (arguments.hasKey("--logs")) {
                String logs = result.toStringFull();
                IO.println(logs);
                FileUtils.saveLogs(logs);
            }
            else {
                IO.println(result);
            }

        }
    }
}
