
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
	
	private Scanner sc;
	private CodeWriter codeWriter;
	private int callCount = 0;
	
	public Parser (CodeWriter codeWriter) {
		this.codeWriter = codeWriter;
	}
	
	// Run the parser, decipher each command
	// use codeWriter to write the output file
	public void parse(String filePath) throws IOException {
		File file = new File(filePath);
		this.sc = new Scanner(file); // file to be scanned
		
		String[] argArray = filePath.split("\\W+"); // split based on non-word character
		String functionName = argArray[argArray.length-2];
		
		while(hasMoreCommands()) {
			String line = sc.nextLine();
			if(line.equals("") || line.substring(0,2).contains("//")) { // whitespace line or comment line
				continue;
			}else {
				line = cleanInlineCom(line);
			}
			// System.out.println(line);
			switch (commandType(line)) {
			case "C_ARITHMETIC":
				// System.out.println("Run C_ARITHMETIC");
				codeWriter.writeArithmetic(line);
				break;
			case "C_PUSH":
				// System.out.println("Run C_PUSH");
				codeWriter.writePush(functionName, arg1(line), arg2(line));
				break;
			case "C_POP":
				// System.out.println("Run C_POP");
				codeWriter.writePop(functionName, arg1(line), arg2(line));
				break;
			case "C_LABEL":
				// System.out.println("Run C_POP");
				codeWriter.writeLabel(arg1(line));
				break;
			case "C_GOTO":
				// System.out.println("Run C_POP");
				codeWriter.writeGoto(arg1(line));
				break;
			case "C_IF":
				// System.out.println("Run C_POP");
				codeWriter.writeIf(arg1(line));
				break;
			case "C_FUNCTION":
				// System.out.println("Run C_POP");
				codeWriter.writeFunction(arg1(line), arg2(line));
				break;
			case "C_CALL":
				// System.out.println("Run C_POP");
				callCount += 1;
				codeWriter.writeCall(arg1(line), arg2(line), callCount);
				break;
			case "C_RETURN":
				// System.out.println("Run C_POP");
				codeWriter.writeReturn();
				break;
			}
		}
		// close the Scanner
		sc.close();
//		try {
//			codeWriter.close();
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	// return True if Scanner has next line
	private boolean hasMoreCommands() { 
		// System.out.println("Run hasMoreCommands method");
		return sc.hasNextLine();
		}
	
	// take a string and return a string with no white space on both sides and no in-line comment
	private String cleanInlineCom(String str) {
		if(str.contains("//")) {
			String[] strSplit = str.split("//");
			str = strSplit[0].strip();
			}else {
				str = str.strip();
			}
		return str;
		}
	
	// take a string and return a string based on what the given string contains
	private String commandType(String str) {
		// System.out.println("Run commandType command");
		if (str.contains("push")){
			// System.out.println("return C_PUSH");
			return "C_PUSH";
		}
		else if (str.contains("pop")) {
			// System.out.println("return C_POP");
			return "C_POP";
		}
		else if (str.contains("label")) {
			return "C_LABEL";
		}
		else if (str.contains("if-goto")) {
			return "C_IF";
		}
		else if (str.contains("goto")) {
			return "C_GOTO";
		}
		else if (str.contains("function")) {
			return "C_FUNCTION";
		}
		else if (str.contains("call")) {
			return "C_CALL";
		}
		else if (str.contains("return")) {
			return "C_RETURN";
		}
		else {
			return "C_ARITHMETIC";
		}
			
	}
	
	// take a string and extract the 1st argument
	private String arg1(String str) { 
		String[] lineSplit = str.split(" ");
		switch (lineSplit[0]) {
		case "label":
		case "goto":
		case "if-goto":
		case "function":
			return lineSplit[1];
		default:
			switch (lineSplit[1]) {
			case "local":
				return "LCL";
			case "argument":
				return "ARG";
			case "this":
				return "THIS";
			case "that":
				return "THAT";
			default:
				return lineSplit[1];
			}
		}
	}
	
	// take a string and extract the 2nd argument
	private String arg2(String str) { 
		String[] lineSplit = str.split(" ");
		// System.out.println("arg2 is " + lineSplit[2]);
		return lineSplit[2]; 
	}
}