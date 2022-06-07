//import java.io.FileWriter;
import java.io.IOException;

/**
 * CompilationEngine.java
 * represent a compilation engine that given a series of tokens, produced a complete VM code
 * Instantiated by the JackTokenizer class 
 * Instantiate a VMWriter to complete the writing of VM code
 * @author danie
 *
 */
public class CompilationEngine {
	
	private VMWriter vmWriter;
	
	private SymbolTable classTable;
	private SymbolTable subroutineTable;
	private String className;
	
	private int ifCount = -1;
	private int whileCount = -1;
	
//	private String outputFileContent = ""; // for XML code
//	private String outputFilePath;
	
	private String[] tokens;
	private int tokenIndex;
	private String currentToken;
	private String currentTag;
	
	// given a series of tokens and an output file path, construct a CompilationEngine
	public CompilationEngine(String tokens, String outputFilePath) {
		vmWriter = new VMWriter(outputFilePath + ".vm");
		
		this.tokens = tokens.split("\n");
		this.tokenIndex = 0;

		classTable = new ClassTable();
		subroutineTable = new SubroutineTable();
//		compileClass();
	}
	
	// write the outputFileContent string into a XML file 
//	public void outputXML() throws IOException{
//		FileWriter writer = new FileWriter(this.outputFilePath);
//		writer.write(outputFileContent);
//		writer.close();
//	}
	
	// consume the current token, throws error if currentToken does not match the given string
	private void eat(String str) {
		currentToken = tokens[tokenIndex].split(" ")[1];
		if((currentToken.equals(str))) {
//		    this.outputFileContent += tokens[tokenIndex] + "\n";
		    this.tokenIndex += 1;
			return;
		}
		else {
			throw new java.lang.Error("currentToken " + currentToken + " does not match " + str);
		}
	}
	
	// consume the current token, get its type
	private String eatType() {
		// int | char | boolean | className
		currentToken = tokens[tokenIndex].split(" ")[1];
		String type;
		
		if (currentToken.equals("int")) {
			eat("int");
			type = "int";
		}
		else if (currentToken.equals("char")) {
			eat("char");
			type = "char";
		}
		else if (currentToken.equals("boolean")) {
			eat("boolean");
			type = "boolean";
		}
		else { // type is a class
			eat(currentToken);
			type = currentToken;
		}
		return type;
	}
	
	// consume an identifier, save it into either classTable or subroutineTable
	private void defineIdentifier(String type, String kind) {
		currentTag = tokens[tokenIndex].split(" ")[0];
		if (currentTag.equals("<identifier>")) {
			currentToken = tokens[tokenIndex].split(" ")[1];
			
			if (kind.equals("static")) { // class-level
				classTable.define(currentToken, type, kind);
//				int index = classTable.indexOf(currentToken);
//				
//				this.outputFileContent += "<" + kind + " " + index + " defined> " + currentToken +
//											" </" + kind + " " + index + " defined>" + "\n";
			}
			else if (kind.equals("field")) { // class-level
//				System.out.println("add" + currentToken + " to the field.");
				classTable.define(currentToken, type, "this");
//				int index = classTable.indexOf(currentToken);
//				
//				this.outputFileContent += "<" + kind + " " + index + " defined> " + currentToken +
//											" </" + kind + " " + index + " defined>" + "\n";
			}
			else if (kind.equals("local") || kind.equals("argument")) { // subroutine-level
				subroutineTable.define(currentToken, type, kind);
//				int index = subroutineTable.indexOf(currentToken);
//				
//				this.outputFileContent += "<" + kind + " " + index + " defined> " + currentToken +
//											" </" + kind + " " + index + " defined>" + "\n";
			}
//			else { // class or subroutine name
//				this.outputFileContent += tokens[tokenIndex] + "\n";
//			}

			this.tokenIndex += 1;
			return;
		}
		else {
			throw new java.lang.Error("currentTag " + tokens[tokenIndex] + " is not a identifier ");
		}
	}
	
	// consume the identifier, get the kind and index of the token, write it into outputFileContent
	// it seems that eat Identifier should delay the writing of VM code, meaning it should return some value
	private String eatIdentifier() {
		currentTag = tokens[tokenIndex].split(" ")[0];
		
		if (currentTag.equals("<identifier>")) {
			currentToken = tokens[tokenIndex].split(" ")[1];
			
//			if(subroutineTable.hasKey(currentToken)) {
//				String kind = subroutineTable.kindOf(currentToken);
//				int index = subroutineTable.indexOf(currentToken);
//				
//				this.outputFileContent += "<" + kind + " " + index + " used> " + currentToken +
//										" </" + kind + " " + index + " defined>" + "\n";
//			}
//			else if(classTable.hasKey(currentToken)) {
//				String kind = classTable.kindOf(currentToken);
//				int index = classTable.indexOf(currentToken);
//				
//				this.outputFileContent += "<" + kind + " " + index + " used> " + currentToken +
//										" </" + kind + " " + index + " defined>" + "\n";
//			}
//			else { // identifier is a class
////				throw new java.lang.Error("currentToken " + currentToken + " not found in the symbol table.");
//				this.outputFileContent += tokens[tokenIndex] + "\n";
//			}
			this.tokenIndex += 1;
			return currentToken;
		}
		else {
			throw new java.lang.Error("currentTag " + tokens[tokenIndex] + " is not a identifier ");
		}
	}
	
	// return true if the current token is "field" or "static"
	private boolean isClassVarDec() {
		currentToken = tokens[tokenIndex].split(" ")[1];
		String classVarDecRegex = "(field|static)";
		return currentToken.matches(classVarDecRegex);
	}
	
	// compile class according to Jack grammar
	public void compileClass() throws IOException{
//		outputFileContent += "<class>" + "\n";
		
		// start the classTable
		classTable.startSubroutine();
		
		// Class
		eat("class");
		// className
		className = tokens[tokenIndex].split(" ")[1];
		defineIdentifier("null", "class"); // no type, class kind
		eat("{");
		// classVarDec*
		while (isClassVarDec()) {
			compileClassVarDec();
		}
		// subroutineDec* until the final "}" symbol
		while (tokenIndex < (tokens.length - 1)) {
			compileSubroutineDec();
		}
		eat("}");
		
//		outputFileContent += "</class>" + "\n";
		
		// output VM code
		vmWriter.close();
		return;
	}
	
	// compile class variable declaration according to Jack grammar
	private void compileClassVarDec() {
//		outputFileContent += "<classVarDec>" + "\n";
		
		// field or static
		String kind = currentToken = tokens[tokenIndex].split(" ")[1];
		String type;
//		currentToken = tokens[tokenIndex].split(" ")[1];
		if (kind.equals("field")) {
			eat("field");
			type = eatType();
			defineIdentifier(type, kind);
		}
		else if (kind.equals("static")) {
			eat("static");
			type = eatType();
			defineIdentifier(type, kind);
		}
		else {
			throw new java.lang.Error("currentToken " + currentToken + " is not field or static ");
		}
//		// int | char | boolean | className
//		eatType();
//		
//		// varName
//		eatIdentifier();
//		
		// ("," varName)*
		currentToken = tokens[tokenIndex].split(" ")[1];
		while(currentToken.equals(",")) {
			eat(",");
			if (kind.equals("field")) {
				defineIdentifier(type, kind);
			}
			else if(kind.equals("static")) {
				defineIdentifier(type, kind);
			}
//			eatIdentifier();
			currentToken = tokens[tokenIndex].split(" ")[1];
		}
		eat(";");
		
//		outputFileContent += "</classVarDec>" + "\n";
		return;
	}
	
	// count how many local variables, until meet any one of token that is a statement	
	private int countVars() {
		// iterate the tokens first to count the number of local variables
		int currentIndex = tokenIndex;
		int nVars = 0;
		while(!isCurrentTokenStatement()) {
			if (currentToken.equals("var") || currentToken.equals(",")) {
				nVars++;
			}
			tokenIndex++;
		}
		// restore the tokenIndex
		tokenIndex = currentIndex;
		return nVars;
	}
	// compile class subroutine declaration according to Jack grammar
	private void compileSubroutineDec() {
//		outputFileContent += "<subroutineDec>" + "\n";
		
		// start a new subroutine
		subroutineTable.startSubroutine();
		String subroutineType;
		
		// constructor | function | method
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("constructor")) {
			subroutineType = "constructor";
			eat("constructor");
		}
		else if (currentToken.equals("function")) {
			subroutineType = "function";
			eat("function");
		}
		else if (currentToken.equals("method")) {
			subroutineType = "method";
			// push this as argument 0;
			subroutineTable.define("this", className, "argument");
			eat("method");
		}
		else {
			throw new java.lang.Error("currentToken " + currentToken + " is not constructor, function or method.");
		}
		// void | type
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("void")) {
			eat("void");
		}
		else {
			eatType();
		}
		// subroutineName
		String subroutineName = tokens[tokenIndex].split(" ")[1];
		defineIdentifier("null", "subroutine");
		// (parameterList)
		eat("(");
		compileParameterList();
		eat(")");
		
		// iterate the tokens first to count the number of local variables
		int nVars = countVars();
		
		// output Function
		String name = className + "." + subroutineName;
		vmWriter.writeFunction(name, nVars);
		
		// set this object depending on subroutineType
		if (subroutineType.equals("constructor")) {
//			System.out.println("call classTable.varCount");
			int fieldCount = classTable.varCount("this");
			vmWriter.writePush("constant", fieldCount);
//			System.out.println("Just write push constant " + fieldCount);
			vmWriter.writeCall("Memory.alloc", 1);
			vmWriter.writePop("pointer", 0);
		}
		else if (subroutineType.equals("method")) {
			vmWriter.writePush("argument", 0);
			vmWriter.writePop("pointer", 0);
		}
		
		// subroutineBody
		compileSubroutineBody();
		
//		outputFileContent += "</subroutineDec>" + "\n";
		return;
	}
	
	// compile parameter list according to Jack grammar
	private void compileParameterList() {
//		outputFileContent += "<parameterList>" + "\n";
		
		// type varName ?
		currentToken = tokens[tokenIndex].split(" ")[1];
		String type;
		String kind = "argument";
		if (!currentToken.equals(")")) { // "(" not followed by ")"
			type = eatType();
			defineIdentifier(type, kind);
			
			// ("," type varName)*
			currentToken = tokens[tokenIndex].split(" ")[1];
			while(currentToken.equals(",")) {
				eat(",");
				type = eatType();
				defineIdentifier(type, kind);
				currentToken = tokens[tokenIndex].split(" ")[1];
			}
		}
		
//		outputFileContent += "</parameterList>" + "\n";
		return;
	}
	
	// compile subroutine body according to Jack grammar
	private void compileSubroutineBody() {
//		outputFileContent += "<subroutineBody>" + "\n";
		
		eat("{");
		// varDec*
		currentToken = tokens[tokenIndex].split(" ")[1];
		while(currentToken.equals("var")) {
			compileVarDec();
			currentToken = tokens[tokenIndex].split(" ")[1];
		}
		compileStatements();
		eat("}");
		
//		outputFileContent += "</subroutineBody>" + "\n";
		return;
	}
	
	// compile local variable declaration according to Jack grammar
	private void compileVarDec() {
//		outputFileContent += "<varDec>" + "\n";
		String type;
		String kind = "local";

		eat("var");
		// type varName
		type = eatType();
		defineIdentifier(type, kind);
		// ("," varName)*
		currentToken = tokens[tokenIndex].split(" ")[1];
		while(currentToken.equals(",")) {
			eat(",");
			defineIdentifier(type, kind);
			currentToken = tokens[tokenIndex].split(" ")[1];
		}
		eat(";");
		
//		outputFileContent += "</varDec>" + "\n";
		return;
	}
	
	// return true if current token is a statement
	private boolean isCurrentTokenStatement() {
		currentToken = tokens[tokenIndex].split(" ")[1];
		String statementRegex = "(let|if|while|do|return)";
		return currentToken.matches(statementRegex);
	}
	
	// compile statements according to Jack grammar
	private void compileStatements() {
//		outputFileContent += "<statements>" + "\n";
//		
		// letStatement | ifStatement | whileStatement | doStatement | 
		while(isCurrentTokenStatement()) {
			switch (currentToken) {
			case "let":
				compileLet();
				break;
			case "if":
				ifCount++;
				compileIf();
				break;
			case "while":
				whileCount++;
				compileWhile();
				break;
			case "do":
				compileDo();
				break;
			case "return":
				compileReturn();
				break;
			}
		}
		
//		outputFileContent += "</statements>" + "\n";
		return;
	}
	
	// compile let statement according to Jack grammar
	private void compileLet() {
//		outputFileContent += "<letStatement>" + "\n";
//		
		eat("let");
		
		// varName ("[" expression "]")?
		String varName = eatIdentifier();
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("[")) { // array
			
			if (subroutineTable.hasKey(varName)) {
				vmWriter.writePush(subroutineTable.kindOf(varName), subroutineTable.indexOf(varName));
			}
			else if (classTable.hasKey(varName)) {
				vmWriter.writePush(classTable.kindOf(varName), classTable.indexOf(varName));
			}
			else {
				throw new java.lang.Error("current symbol " + varName + " not found in symbol table");
			}
			
			eat("[");
			compileExpression();
			eat("]");
			vmWriter.writeArithmetic("add");
			eat("=");
			compileExpression();
			eat(";");
			// store right-hand side value, get address of array, push back right-hand side value, assign value to array
			vmWriter.writePop("temp", 0);
			vmWriter.writePop("pointer", 1);
			vmWriter.writePush("temp", 0);
			vmWriter.writePop("that", 0);
		}
		else {
			eat("=");
			compileExpression();
			eat(";");
			// pop varName
			if (subroutineTable.hasKey(varName)) {
				vmWriter.writePop(subroutineTable.kindOf(varName), subroutineTable.indexOf(varName));
			}
			else if (classTable.hasKey(varName)) {
				vmWriter.writePop(classTable.kindOf(varName), classTable.indexOf(varName));
			}
			else {
				throw new java.lang.Error("current symbol " + varName + " not found in symbol table");
			}
		}
//
//		outputFileContent += "</letStatement>" + "\n";
		return;
	}
	
	// compile if statement according to Jack grammar
	// !!!
	private void compileIf() {
//		outputFileContent += "<ifStatement>" + "\n";
		int count = ifCount;
		String label;
		// if statement
		eat("if");
		eat("(");
		compileExpression();
		eat(")");
		
		// not, if-goto L1
		vmWriter.writeArithmetic("not");
		label = "IF_TRUE" + count;
		vmWriter.writeIf(label);
		
		eat("{");
		compileStatements();
		eat("}");
		
		// goto L2
		label = "IF_END" + count;
		vmWriter.writeGoto(label);
		// label L1
		label = "IF_TRUE" + count;
		vmWriter.writeLabel(label);
		// else statement
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("else")) {
			eat("else");
			eat("{");
			compileStatements();
			eat("}");
		}
		// label L2
		label = "IF_END" + count;
		vmWriter.writeLabel(label);
		
//		outputFileContent += "</ifStatement>" + "\n";
		return;
	}
	
	// compile while statement according to Jack grammar
	private void compileWhile() {
//		outputFileContent += "<whileStatement>" + "\n";
		int count = whileCount;
		String label;
		// label L1
		label = "WHILE_EXP"+ count;
		vmWriter.writeLabel(label);
		
		eat("while");
		eat("(");
		compileExpression();
		eat(")");
		
		// not, if-goto L2
		vmWriter.writeArithmetic("not");
		label = "WHILE_END" + count;
		vmWriter.writeIf(label);
		
		eat("{");
		compileStatements();
		eat("}");
		
		// goto L1
		label = "WHILE_EXP"+ count;
		vmWriter.writeGoto(label);
		// label L2
		label = "WHILE_END" + count;
		vmWriter.writeLabel(label);
//		
//		outputFileContent += "</whileStatement>" + "\n";
		return;
	}
	
	// compile do statement according to Jack grammar
	private void compileDo() {
//		outputFileContent += "<doStatement>" + "\n";
		
		eat("do");
		// subroutineCall
		String token = eatIdentifier();
		currentToken = tokens[tokenIndex].split(" ")[1];
		if(currentToken.equals(".")) {
			eat(".");
			String subroutineName = eatIdentifier();
			eat("(");
			
			// must count how many arguments there are
			int nArgs = 0;
			String classType;
			// distinguish token is a varName or className
			if(subroutineTable.hasKey(token)) { 
				nArgs++;
				vmWriter.writePush(subroutineTable.kindOf(token), subroutineTable.indexOf(token));
				classType = subroutineTable.typeOf(token);
			}
			else if (classTable.hasKey(token)) {
				nArgs++;
				vmWriter.writePush(classTable.kindOf(token), classTable.indexOf(token));
				classType = classTable.typeOf(token);
			}
			else {
				classType = token;
			}
			
			nArgs += compileExpressionList();
			eat(")");
			// output subroutineCall
			String name = classType + "." + subroutineName;
			vmWriter.writeCall(name, nArgs);
		}
		else {
			eat("(");
			// must count how many arguments there are
			int nArgs = 1;
			// current object is the first argument
			vmWriter.writePush("pointer", 0);
			nArgs += compileExpressionList();
			eat(")");
			
			// output subroutineCall
			String name = className + "." + token;
			vmWriter.writeCall(name, nArgs);
		}
		
		eat(";");
		
		// dump dummy value 0
		vmWriter.writePop("temp", 0);
		
//		outputFileContent += "</doStatement>" + "\n";
		return;
	}
	
	// compile return statement according to Jack grammar
	private void compileReturn() {
//		outputFileContent += "<returnStatement>" + "\n";
		
		eat("return");
		
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (!currentToken.equals(";")) {
			compileExpression();
		}
		else {
			vmWriter.writePush("constant", 0); // dummy value
		}
		eat(";");
		
		// output Return
		vmWriter.writeReturn();
		
//		outputFileContent += "</returnStatement>" + "\n";
		return;
	}
	
	private boolean isCurrentTokenOp() {
		currentToken = tokens[tokenIndex].split(" ")[1];
		String operatorRegex = "[\\p{Punct}&&[^!~\"#$%&'(),.:;<>?@[\\]^`{}_]]]|(&amp;|&lt;|&gt;)";
		return currentToken.matches(operatorRegex);
	}
	// compile expression according to Jack grammar
	private void compileExpression() {
//		outputFileContent += "<expression>" + "\n";
		// term
		compileTerm();
		// (op term)*
		while (isCurrentTokenOp()) {
			String op = currentToken;
			eat(op); // op
			compileTerm();
			
			// output op
			switch(op) {
			case "+":
				vmWriter.writeArithmetic("add");
				break;
			case "-":
				vmWriter.writeArithmetic("sub");
				break;
			case "*":
				vmWriter.writeCall("Math.multiply", 2);
				break;
			case "/":
				vmWriter.writeCall("Math.divide", 2);
				break;
			case "&amp;":
				vmWriter.writeArithmetic("and");
				break;
			case "|":
				vmWriter.writeArithmetic("or");
				break;
			case "&lt;":
				vmWriter.writeArithmetic("lt");
				break;
			case "&gt;":
				vmWriter.writeArithmetic("gt");
				break;
			case "=":
				vmWriter.writeArithmetic("eq");
				break;
			}
		}
		
//		outputFileContent += "</expression>" + "\n";
		return;
	}
	
	private boolean isKeywordConst() {
		currentToken = tokens[tokenIndex].split(" ")[1];
		String keywordConstRegex = "(true|false|null|this)";
		return currentToken.matches(keywordConstRegex);
	}
	
	private boolean isUnaryOp() {
		currentToken = tokens[tokenIndex].split(" ")[1];
		String unaryOpRegex = "[-~]";
		return currentToken.matches(unaryOpRegex);
	}
	
	// compile term according to Jack grammar
	private void compileTerm() {
//		outputFileContent += "<term>" + "\n";
		
		currentTag = tokens[tokenIndex].split(" ")[0];
		currentToken = tokens[tokenIndex].split(" ")[1];

		switch (currentTag) {
		case "<integerConstant>":
			eat(currentToken);
			vmWriter.writePush("constant", Integer.parseInt(currentToken)); // push constant currentToken
			break;
		case "<stringConstant>":
			String[] strArray = tokens[tokenIndex].split("[<>]");
			String str = strArray[2];
			// create string object
			int strLength = str.length() - 2;

			vmWriter.writePush("constant", strLength);
			vmWriter.writeCall("String.new", 1);
			// append string character
			char[] chars = str.substring(1, strLength+1).toCharArray();
			for (char ch: chars) {
	            int ascii = (int) ch;
				vmWriter.writePush("constant", ascii);
				vmWriter.writeCall("String.appendChar", 2);
	        }
			eat(currentToken);
			break;
		// keywordConstant
		case "<keyword>":
			if(isKeywordConst()) {
				String keyConst = currentToken;
				eat(keyConst);
				
				// output keyConst
				switch(keyConst) {
				case "true":
					vmWriter.writePush("constant", 1);
					vmWriter.writeArithmetic("neg");
					break;
				case "false":
				case "null":
					vmWriter.writePush("constant", 0);
					break;
				case "this":
					vmWriter.writePush("pointer", 0);
					break;
				}
			}
			else {
				throw new java.lang.Error("current keyword " + currentToken + " is not a keywordConstant");
			}
			break;
		// unaryOp
		case "<symbol>":
			if (isUnaryOp()) { // unartOp term
				String unaryOp = currentToken;
				eat(unaryOp);
				compileTerm();
				
				// output unaryOp
				switch(unaryOp) {
				case "-":
					vmWriter.writeArithmetic("neg");
					break;
				case "~":
					vmWriter.writeArithmetic("not");
					break;
				}
			}
			else if (currentToken.equals("(")) {
				eat("(");
				compileExpression();
				eat(")");
			}
			else {
				throw new java.lang.Error("current symbol " + currentToken + " is not a unaryOp nor \"(\"");
			}
			break;
			
		case "<identifier>":
			String token = eatIdentifier(); // varName | subroutine call 

			currentToken = tokens[tokenIndex].split(" ")[1];
			if (currentToken.equals("[")) { // varName (array)
				if (subroutineTable.hasKey(token)) {
					vmWriter.writePush(subroutineTable.kindOf(token), subroutineTable.indexOf(token));
				}
				else if (classTable.hasKey(token)) {
					vmWriter.writePush(classTable.kindOf(token), classTable.indexOf(token));
				}
				else {
					throw new java.lang.Error("current symbol " + token + " not found in symbol table");
				}
				
				eat("[");
				compileExpression();
				eat("]");
				
				// add, pop pointer 1, push that 0
				vmWriter.writeArithmetic("add");
				vmWriter.writePop("pointer", 1);
				vmWriter.writePush("that", 0);
			}
			else if (currentToken.equals(".")) { // subroutine call
				eat(".");
				String subroutineName = eatIdentifier();
				eat("(");
				// must count how many arguments there are
				int nArgs = 0;
				
				// distinguish token is a varName or className
				String classType;
				if(subroutineTable.hasKey(token)) { 
					nArgs++;
					vmWriter.writePush(subroutineTable.kindOf(token), subroutineTable.indexOf(token));
					classType = subroutineTable.typeOf(token);
				}
				else if (classTable.hasKey(token)) {
					nArgs++;
					vmWriter.writePush(classTable.kindOf(token), classTable.indexOf(token));
					classType = classTable.typeOf(token);
				}
				else {
					classType = token;
				}
				
				nArgs += compileExpressionList();
				eat(")");
				
				// output subroutineCall
				String name = classType + "." + subroutineName;
				vmWriter.writeCall(name, nArgs);
			}
			else if (currentToken.equals("(")) { // subroutine call
				eat("(");
				
				// must count how many arguments there are
				int nArgs = 1;
				
				// current object is the first argument
				vmWriter.writePush("pointer", 0);
				nArgs += compileExpressionList();
				eat(")");
				
				// output subroutineCall
				String name = className + "." + token;
				vmWriter.writeCall(name, nArgs);
			}
			else { // varName
				String kind;
				int index;
				if(subroutineTable.hasKey(token)) { 
					kind = subroutineTable.kindOf(token);
					index = subroutineTable.indexOf(token);
				}
				else if (classTable.hasKey(token)) {
					kind = classTable.kindOf(token);
					index = classTable.indexOf(token);
				}
				else {
					throw new java.lang.Error("current symbol " + token + " not found in symbol table");
				}
				
				vmWriter.writePush(kind, index);
			}
			break;
		}
//		outputFileContent += "</term>" + "\n";
		return;
	}
	
	// compile expression list according to Jack grammar
	private int compileExpressionList() {
//		outputFileContent += "<expressionList>" + "\n";
		
		int nArgs = 0;
		// type varName ?
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (!currentToken.equals(")")) { // "(" not followed by ")"
			compileExpression();
			nArgs++;
			
			// ("," type varName)*
			while(currentToken.equals(",")) {
				eat(",");
				compileExpression();
				nArgs++;
			}
		}
//		outputFileContent += "</expressionList>" + "\n";		
		return nArgs;
	}
}
