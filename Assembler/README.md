# Hack Assembler
This is a assembler program written in Java that translates Hack assembly programs into executable Hack binary code.

In the src/assembler, you can find 4 main Java classes. Each class serves separate function as described below:

## HackAssembler class
The main programm of the Hack Assembler.
It create Parser, Comp, SymbolTable objects, initializes the I/O files, and drives the whole assembling process.
 
## Parser class
The Parser class is responsible for unpacking each line instruction into its underlying fields.

## Comp class
The Comp class does its work after the Parser class turns the instructions into fields.

It takes those fields one by one and translates them into its corresponding binary values.

These binary values are then get written into the hack files in the HackAssembler class.

## SymbolTable class
The SymbolTable class manages the symbol table.

In the initialize() method, it defines the translation of different symbols in the Hack assembly languages
into their corresponding binary codes.

It provides some getter method for Parser and Comp class to retrieve certain binary value associated with the symbol.

# How to use the Hack Assembler
1. Download the source files in the src folder
2. Open the command prompt, type "java HackAssembler Xxx.asm", where Xxx.asm is the file you wish to translate. Some files are provided in files folder.
3. The command should produce a Xxx.hack that can be executed on the Hack computer.

You can get the Hack computer from this link:
https://www.nand2tetris.org/software