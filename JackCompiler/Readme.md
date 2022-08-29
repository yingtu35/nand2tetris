# Jack Compiler
This is a program written in Java that translates the Jack program source code into the Hack VM code.

It compiles any given "error free" Jack files and produce VM codes.

In the src, you can find 3 main Java classes. Each class serves separate function as described below:
## JackCompiler
The main programm of the Jack Compiler.
It create a JackTokenizer object, initializes the I/O files, and drives the whole compiling process.

## JackTokenizer
The JackTokenizer class is responsible for converting Jack program source code into tokens.

There are five types of tokens: symbol, stringConstant, integerConstant, keyword and identifier.

In particularly, the run() method iterate the given file character by character to capture each token.

## CompilationEngine
The CompilationEngine class is responsible for taking a series of tokens and producing the complete VM code.

It creates a VMWriter object to delegate the vm code writing duties to the VMWriter.

There are multiple compileXxx methods in the CompilationEngine class. Each method is responsible for handling the tokens that make up Xxx and
advancing the tokenizer exactly beyond the tokens.

## VMWriter
The VMWriter is responsible for writing out the VM code into the output file.

There are multiple writeXxx methods in the VMWriter. Each method takes in parameters associated with the specific command and write out the VM code.

## SymbolTable
The SymbolTable class is an abstract class that is inherited by ClassTable class and SubroutineTable class.

It provides simple getter method to retrieve kind, type or index of a symbol.

## ClassTable
The ClassTable class represent the class-level symbol table.

It keeps track of field and static variables in the file.

## SubroutineTable
The SubroutineTable class represent the subroutine-level symbol table.

It keeps track of local variables and argument variables in each subroutine.

# How to use the JackCompiler
1. Download the source files in the src folder
2. Compile JackCompiler.java (e.g. type "javac JackCompiler.java" in Command Prompt)
3. Compile code files written in Jack by one of the following ways:
	1. provide a file path to the JackCompiler, it will compile the file and output a VM file
	2. You can also provide a directory to the JackCompiler, it will recognize all the Jack files in the directory, and produce VM files in the directory.
4. To inspect the produced VM files, you can use the VMEmulator (provided at Nand2Tetris website) to run the VM files

For more information about the VMEmulator, please visit the link:
https://www.nand2tetris.org/software


