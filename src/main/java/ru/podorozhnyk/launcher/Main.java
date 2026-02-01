package ru.podorozhnyk.launcher;/*
 * - Сделать сортировку без аргументов (текущая папка)
 * - Сортировщик определяет типы файлов (документ, изображение, видео, и т.д.)
 * - Затем на каждый найденный тип создаётся папка в текущем каталоге
 * - Затем помеченные файлы каждого типа перемещаются в подкаталог своего типа

 - Следует добавить параметр, позволяющий указать уровень вложенности.
 - Добавить логгер ошибок и пропущенных файлов
 аргументы: -d=... (max-depth)
            --path=
            --gui
 */

import ru.podorozhnyk.application.Organizer;
import ru.podorozhnyk.utils.Arguments;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {


    void main(String[] args) {
        Arguments arguments = Arguments.from(args);
        if (arguments.hasKey("--gui")) {
            IO.println("Work in GUI");
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
            new Organizer(workingPath, maxDepth);
        }


    }
}
