import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** JackTokenizer.java
 * 	represent a JackTokenizer that convert a jack file to a series of tokens
 * 	to run it, use JackAnalyzer class and provide the path of the jack file as the argument
 * 	JackAnalyzer class will create a JackTokenizer object and start the tokenization
 *  A CompilationEngine object will be instantiated to complete the parsing and VM code generation
 * @author danie
 *
 */
public class JackTokenizer {
	
	private String inputFileContent = "";
	private String outputFileContent = "";
	private String inputFilePath;
	private String outputFilePath;
	private int index; 							// indicate the position we are at the inputFileContent

	private boolean multipleComment = false; 	// if current line is part of a multiple-line comment
	private boolean strConstant = false; 		// if current character is part of a string constant
	
	private Set<String> keywordSet = Stream.of("class", "constructor", "function", "method", "field", "static",
											"var", "int", "char", "boolean", "void", "true", "false", "null",
											"this", "let", "do", "if", "else", "while", "return")
	         						.collect(Collectors.toCollection(HashSet::new));
	
	// given inputFilePath, construct a JackTokenizer
	public JackTokenizer(String inputFilePath) throws IOException{
		this.inputFilePath = inputFilePath;
				
		cleanFile();
	}
	
	// return true if the string is numeric
	public static boolean isNumeric(String str) {
		  return str.matches("-?\\d+");  //match a integer with optional '-'
		}
	
	// return true if the string is a symbol specified by Jack rules
	public static boolean containsSymbol(String str) {
		  return str.matches("[\\p{Punct}&&[^\"_]]");  //match a integer with optional '-'
		}
	
	// return true if the string belongs to the keyword set
	public boolean isKeyword(String str) {
		  return keywordSet.contains(str);  //match a integer with optional '-'
		}
	
	// Iterate the inputFileContent character by character to capture each token
	public void run() throws IOException{
		
		this.index = 0;
		String currentToken = "";
		// main loop
		while(hasMoreTokens()) {
			// !!!
			char chr = inputFileContent.charAt(index);
			
			if (chr == '\"') {
				strConstant = !strConstant;
				currentToken += chr;
			}
			else if (strConstant) {
				currentToken += chr;
			}
			else if (Character.isWhitespace(chr)) { // encounter whitespace
				if(!(currentToken.isEmpty())) {
					writeToken(currentToken);
					currentToken = "";
				}
			}
			else if (containsSymbol(Character.toString(chr))) { // encounter a symbol
				if(!(currentToken.isEmpty())) {
					writeToken(currentToken);
					currentToken = ""; // reinitialize
				}
				writeToken(Character.toString(chr));
			}
			else {
				currentToken += chr;
			}
			advance(); // advance to next character
		}
		parse();
//		outputXML();
	}
	
	// take a string and return a string with no white space on both sides and no in-line comment
	private void cleanFile() throws IOException{
		File file = new File(inputFilePath);
		Scanner sc = new Scanner(file);
		
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			 if(line.contains("*/")) {
				multipleComment = false;
				continue;
			}
			else if(multipleComment || line.equals("") || (line.contains("//") && line.substring(0, 2).equals("//"))) { // whitespace line or comment line
				continue;
			}
			else if(line.contains("/**")) {
				multipleComment = true;
				continue;
			}
			else {
				// clean in-line comment and space
				if(line.contains("//")) {
					String[] strSplit = line.split("//");
					line = strSplit[0].strip();
				}
				else {
					line = line.strip();
				}
			}
			
			inputFileContent = inputFileContent.concat(line + "\n");
		}
		sc.close();
		
		return;
	}
	
	// handle the parsing job to the compilationEngine
	private void parse() throws IOException{
		this.outputFilePath = inputFilePath.split("\\.")[0];
		CompilationEngine parser = new CompilationEngine(outputFileContent, outputFilePath);
		parser.compileClass();
//		parser.outputXML();
	}
	
	// return true if current index is less than length of the inpurFileContent string
	private boolean hasMoreTokens() {
		return this.index < inputFileContent.length();
	}
	
	// advance the index to the next
	private void advance() {
		this.index += 1;
	}
	
	// write the token into the outputFileContent in XML form
	private void writeToken(String token) {
		String xmlString = "";
		switch (tokenType(token)) {
		case "SYMBOL":
			xmlString = "<symbol> " + symbol(token) + " </symbol>";
			break;
		case "STRING_CONST":
			xmlString = "<stringConstant> " + stringVal(token) + " </stringConstant>";
			break;
		case "INT_CONST":
			xmlString = "<integerConstant> " + intVal(token) + " </integerConstant>";
			break;
		case "KEYWORD":
			xmlString = "<keyword> " + keyword(token) + " </keyword>";
			break;
		case "IDENTIFIER":
			xmlString = "<identifier> " + identifier(token) + " </identifier>";
			break;
		}
		outputFileContent = outputFileContent.concat(xmlString + "\n");
	}
	
	// determine the type of token string based on Jack rules
	private String tokenType(String token) {
		// Symbol
		if (containsSymbol(token)) {
			return "SYMBOL";
		}
		// String constant
		else if (token.contains("\"")) {
			return "STRING_CONST";
		}
		// integer constant
		else if (isNumeric(token)) {
			return "INT_CONST";
		}
		// keyword
		else if (isKeyword(token)) {
			return "KEYWORD";
		}
		// identifier
		else {
			return "IDENTIFIER";
		}
	}
	
	// get the keyword from the token
	private String keyword(String token) {
		return token;
	}
	
	// get the symbol of XML form from the token
	private String symbol(String token) {
		if (token.equals("<")) {
			token = "&lt;";
		}
		else if (token.equals(">")) {
			token = "&gt;";
		}
		else if (token.equals("\"")) {
			token = "&quot;";
		}
		else if (token.equals("&")) {
			token = "&amp;";
		}
		return token;
	}
	
	// get the identifier from the token
	private String identifier(String token) {
		return token;
	}
	
	// get the integer value from the token
	private String intVal(String token) {
		return token;
	}
	
	// get the string constant from the token
	private String stringVal(String token) {
		return token.replace("\"", "");
	}
}
