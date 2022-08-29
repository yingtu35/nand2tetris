# VM Translator
This is a program written in Java that translates intermediate-level Hack VM code into assembly code.

In the src, you can find 3 main Java classes. Each class serves separate function as described below:

## VMTranslator class
The main programm of the VM Translator.
It create Parser and CodeWriter objects, initializes the I/O files, and drives the whole translating process.
 
## Parser class
The Parser class is responsible for parsing each VM command into its lexical elements.

In particularly, the parse() method handles input file, parse the command and delegate the work to the CodeWriter for writing out the assembly code.

## CodeWriter class
The CodeWriter class is responsible for taking the parsed components from the Parser class, and
write out the assembly code according to the given components.

# How to use the VM Translator
1. Download the source files in the src folder
2. There are two ways you can use the VM Translator:
	- Provide the VMTranslator with a single vm file.
		- Open the command prompt, type "java VMTranslator Xxx.vm", where Xxx.asm is the file you wish to convert. Some files are provided in files folder.
		- The command should produce a Xxx.asm that includes the corresponding assembly code.
	- Provide the VMTranslator with a folder of multiple vm files.
		- Open the command prompt, type "java VMTranslator Xxx", where Xxx is the folder that contains vm files.
		- The VMTranslator will search through the folder and convert every file that ends with ".vm" to assembly code.
		- The command should produce multiple files ending with ".asm" which includes the corresponding assembly code.
