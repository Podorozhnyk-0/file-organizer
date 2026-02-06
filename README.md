# File Organizer

This utility automatically sorts files with various extensions into their respective directories.
The program supports specifying the maximum file search depth (default is 1), enabling logging to the file, and also has the option to run in graphical and console displays.


Launch methods:
1. `java -jar FileOrganizer.jar` - starts sorting in the current directory with maximum depth is 1.
2. `java -jar FileOrganizer.jar --path=... -d=5`- starts sorting in the specified directory with maximum depth is 5.
3. `java -jar FileOrganizer.jar -d=-1` - sets maximum depth to `Integer.MAX_VALUE`.
4. `java -jar FileOrganizer.jar --logs` - enables logging to file.
5. `java -jar FileOrganizer.jar --gui` - runs program in GUI mode.