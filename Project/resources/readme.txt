Data Repository

============== Installation ======================

Create a new Data Repository by adding a new file.

You can add a new file with the command

data-repository add [--description <description>] [--move] [--verbose] <repository path> <file/folder>

============== Usage =============================

The Data Repository has the following basic commands:

add: Adds a data set to the repository. 
replace: Replaces a data set in the repository. 
delete: Deletes a data set in the repository. 
export: Exports a data set of the repository to a specified directory. 
list: Lists meta data of data sets.

Below you will find a list of parameters for the commands.

data-repository add [--description <description>] [--move] [--verbose] <repository path> <file/folder>
The specified file/folder will be added to the repository. Without the --move option a copy will be created. With the --move option the file/folder is moved completely into the repository.

data-repository replace [--description <description>] [--move] [--verbose] <repository path> <data set identifier> <file/folder>
Replaces the data set of specified identifier completely by the specified file/folder. If no description has been specified the old description is kept.

data-repository delete [--id <identifier>] [--name <name>] [--text <text snippet>] [--before <time stamp>] [--after <time stamp>] <repository path> [<data set identifier>]
Delete data sets completely from the repository.

data-repository export [--id <identifier>] [--name <name>] [--text <text snippet>] [--before <time stamp>] [--after <time stamp>] [--verbose] <repository path> [<data set identifier>] <destination folder>
Copies data sets to the destination folder specified by an absolute or relative path. The exported files/folders have the original name.

data-repository list [--id <identifier>] [--name <name>] [--text <text snippet>] [--before <time stamp>] [--after <time stamp>] <repository path>
Lists meta data of data sets matching all criteria.

If you need help with any of the commands, you can also consult data-repository help