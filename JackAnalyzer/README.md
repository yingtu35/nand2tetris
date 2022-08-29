# Jack Analyzer
This is a program written in Java that analyze the syntax of the Jack program source code and output a XML file.

In the src, you can find 3 main Java classes. Each class serves separate function as described below:

## JackAnalyzer class
The main programm of the Jack Analyzer.
It create a JackTokenizer, initializes the I/O files, and drives the whole analyzing process.
 
## JackTokenizer class
The JackTokenizer class is responsible for converting Jack program source code into tokens.

There are five types of tokens: symbol, stringConstant, integerConstant, keyword and identifier.

In particularly, the run() method iterate the given file character by character to capture each token.

## CompilationEngine class
The CompilationEngine class is responsible for taking a series of tokens and producing an analyzed and tokenized XML file.

There are multiple compilexxx methods in the CompilationEngine class. Each method is responsible for handling the tokens that make up xxx and
advancing the tokenizer exactly beyond the tokens.

# How to use the VM Translator
1. Download the source files in the src folder
2. There are two ways you can use the Jack Analyzer:
	- Provide the JackAnalyzer with a single vm file.
		- Open the command prompt, type "java JackAnalyzer Xxx.jack", where Xxx.jack is the file you wish to analyze. Some files are provided in files folder.
		- The command should produce a Xxx.xml that includes xml markup language.
	- Provide the JackAnalyzer with a folder of multiple vm files.
		- Open the command prompt, type "java JackAnalyzer Xxx", where Xxx is the folder that contains jack files.
		- The JackAnalyzer will search through the folder and convert every file that ends with ".jack" to xml file.
		- The command should produce multiple files ending with ".xml" which include xml markup language.
