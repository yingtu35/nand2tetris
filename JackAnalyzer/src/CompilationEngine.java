import java.io.FileWriter;
import java.io.IOException;

/**
 * CompilationEngine.java
 * represent a compilation engine that given a series of tokens, produced a analyzed and tokenized XML code
 * Instantiated by the JackTokenizer class 
 * @author danie
 *
 */
public class CompilationEngine {
	
	private String outputFileContent = "";
	private String outputFilePath;
	
	private String[] tokens;
	private int tokenIndex;
	private String currentToken;
	private String currentTag;
	
	// !!!
	public CompilationEngine(String tokens, String outputFilePath) {
		this.tokens = tokens.split("\n");
		this.tokenIndex = 0;
		this.outputFilePath = outputFilePath;
		
//		compileClass();
	}
	
	// write the outputFileContent string into a XML file 
	public void outputXML() throws IOException{
		FileWriter writer = new FileWriter(this.outputFilePath);
		writer.write(outputFileContent);
		writer.close();
	}
	
	// consume the current token, write it into outputFileContent if it matches the given string
	// otherwise throws a exception
	private void eat(String str) {
		currentToken = tokens[tokenIndex].split(" ")[1];
		if((currentToken.equals(str))) {
		    this.outputFileContent += tokens[tokenIndex] + "\n";
		    this.tokenIndex += 1;
			return;
		}
		else {
			throw new java.lang.Error("currentToken " + currentToken + " does not match " + str);
		}
	}
	
	// consume the current token, write it into outputFileContent if it matches a certain type
	private void eatType() {
		// int | char | boolean | className
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("int")) {
			eat("int");
		}
		else if (currentToken.equals("char")) {
			eat("char");
		}
		else if (currentToken.equals("boolean")) {
			eat("boolean");
		}
		else {
			eatIdentifier(); // type is a class
		}
		return;
	}
	
	// consume the current token, write it into outputFileContent if its tag is identifier
	private void eatIdentifier() {
		currentTag = tokens[tokenIndex].split(" ")[0];
		if (currentTag.equals("<identifier>")) {
			this.outputFileContent += tokens[tokenIndex] + "\n";
			this.tokenIndex += 1;
			return;
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
	public void compileClass() {
		outputFileContent += "<class>" + "\n";
		
		// Class
		eat("class");
		// className
		eatIdentifier();
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
		
		outputFileContent += "</class>" + "\n";
		return;
	}
	
	// compile class variable declaration according to Jack grammar
	private void compileClassVarDec() {
		outputFileContent += "<classVarDec>" + "\n";
		
		// field or static
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("field")) {
			eat("field");
		}
		else if (currentToken.equals("static")) {
			eat("static");
		}
		else {
			throw new java.lang.Error("currentToken " + currentToken + " is not field or static ");
		}
		// int | char | boolean | className
		eatType();
		
		// varName
		eatIdentifier();
		
		// ("," varName)*
		currentToken = tokens[tokenIndex].split(" ")[1];
		while(currentToken.equals(",")) {
			eat(",");
			eatIdentifier();
			currentToken = tokens[tokenIndex].split(" ")[1];
		}
		eat(";");
		
		outputFileContent += "</classVarDec>" + "\n";
		return;
	}
	
	// compile class subroutine declaration according to Jack grammar
	private void compileSubroutineDec() {
		outputFileContent += "<subroutineDec>" + "\n";
		
		// constructor | function | method
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("constructor")) {
			eat("constructor");
		}
		else if (currentToken.equals("function")) {
			eat("function");
		}
		else if (currentToken.equals("method")) {
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
		eatIdentifier();
		// (parameterList)
		eat("(");
		compileParameterList();
		eat(")");
		// subroutineBody
		compileSubroutineBody();
		
		outputFileContent += "</subroutineDec>" + "\n";
		return;
	}
	
	// compile parameter list according to Jack grammar
	private void compileParameterList() {
		outputFileContent += "<parameterList>" + "\n";
		
		// type varName ?
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (!currentToken.equals(")")) { // "(" not followed by ")"
			eatType();
			eatIdentifier();
			
			// ("," type varName)*
			currentToken = tokens[tokenIndex].split(" ")[1];
			while(currentToken.equals(",")) {
				eat(",");
				eatType();
				eatIdentifier();
				currentToken = tokens[tokenIndex].split(" ")[1];
			}
		}
		
		outputFileContent += "</parameterList>" + "\n";
		return;
	}
	
	// compile subroutine body according to Jack grammar
	private void compileSubroutineBody() {
		outputFileContent += "<subroutineBody>" + "\n";
		
		eat("{");
		// varDec*
		currentToken = tokens[tokenIndex].split(" ")[1];
		while(currentToken.equals("var")) {
			compileVarDec();
			currentToken = tokens[tokenIndex].split(" ")[1];
		}
//		System.out.println("Enter compileStatements.");
		compileStatements();
//		System.out.println("Out of compileStatements.");
		eat("}");
		
		outputFileContent += "</subroutineBody>" + "\n";
		return;
	}
	
	// compile local variable declaration according to Jack grammar
	private void compileVarDec() {
		outputFileContent += "<varDec>" + "\n";
//		System.out.println("run varDec");
		// var
		eat("var");
		// type varName
		eatType();
		eatIdentifier();
		// ("," varName)*
		currentToken = tokens[tokenIndex].split(" ")[1];
		while(currentToken.equals(",")) {
			eat(",");
			eatIdentifier();
			currentToken = tokens[tokenIndex].split(" ")[1];
		}
		eat(";");
		
		outputFileContent += "</varDec>" + "\n";
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
		outputFileContent += "<statements>" + "\n";
		
		// letStatement | ifStatement | whileStatement | doStatement | 
		
		while(isCurrentTokenStatement()) {
			switch (currentToken) {
			case "let":
//				System.out.println("run let");
				compileLet();
				break;
			case "if":
//				System.out.println("run if");
				compileIf();
				break;
			case "while":
//				System.out.println("run while");
				compileWhile();
				break;
			case "do":
//				System.out.println("run do");
				compileDo();
				break;
			case "return":
//				System.out.println("run return");
				compileReturn();
				break;
			}
		}
		
		outputFileContent += "</statements>" + "\n";
		return;
	}
	
	// compile let statement according to Jack grammar
	private void compileLet() {
		outputFileContent += "<letStatement>" + "\n";
		
		eat("let");
		
		// varName ("[" expression "]")?
		eatIdentifier();
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("[")) {
			eat("[");
			compileExpression();
			eat("]");
		}
		
		eat("=");
		compileExpression();
		eat(";");
		
		outputFileContent += "</letStatement>" + "\n";
		return;
	}
	
	// compile if statement according to Jack grammar
	private void compileIf() {
		outputFileContent += "<ifStatement>" + "\n";
		
		// if statement
		eat("if");
		eat("(");
		compileExpression();
		eat(")");
		eat("{");
		compileStatements();
		eat("}");
		
		// else statement
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (currentToken.equals("else")) {
			eat("else");
			eat("{");
			compileStatements();
			eat("}");
		}
		
		outputFileContent += "</ifStatement>" + "\n";
		return;
	}
	
	// compile while statement according to Jack grammar
	private void compileWhile() {
		outputFileContent += "<whileStatement>" + "\n";
		
		eat("while");
		eat("(");
		compileExpression();
		eat(")");
		eat("{");
		compileStatements();
		eat("}");
		
		outputFileContent += "</whileStatement>" + "\n";
		return;
	}
	
	// compile do statement according to Jack grammar
	private void compileDo() {
		outputFileContent += "<doStatement>" + "\n";
		
		eat("do");
		// subroutineCall
		eatIdentifier();
		currentToken = tokens[tokenIndex].split(" ")[1];
		if(currentToken.equals(".")) {
			eat(".");
			eatIdentifier();
		}
		eat("(");
		compileExpressionList();
		eat(")");
		eat(";");
		
		outputFileContent += "</doStatement>" + "\n";
		return;
	}
	
	// compile return statement according to Jack grammar
	private void compileReturn() {
		outputFileContent += "<returnStatement>" + "\n";
		
		eat("return");
		
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (!currentToken.equals(";")) {
			compileExpression();
		}
		eat(";");
		
		outputFileContent += "</returnStatement>" + "\n";
		return;
	}
	
	private boolean isCurrentTokenOp() {
		currentToken = tokens[tokenIndex].split(" ")[1];
		String operatorRegex = "[\\p{Punct}&&[^!~\"#$%&'(),.:;<>?@[\\]^`{}_]]]|(&amp;|&lt;|&gt;)";
		return currentToken.matches(operatorRegex);
	}
	// compile expression according to Jack grammar
	// !!!
	private void compileExpression() {
		outputFileContent += "<expression>" + "\n";
//		System.out.println("run expression");
		// term
		compileTerm();
		// (op term)*
		while (isCurrentTokenOp()) {
//			System.out.println("run Op");
			eat(currentToken); // op
			compileTerm();
		}
		
		outputFileContent += "</expression>" + "\n";
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
	// !!!
	private void compileTerm() {
		outputFileContent += "<term>" + "\n";
//		System.out.println("runTerm");
		
		currentTag = tokens[tokenIndex].split(" ")[0];
		currentToken = tokens[tokenIndex].split(" ")[1];

		switch (currentTag) {
		case "<integerConstant>":
			eat(currentToken);
			break;
		case "<stringConstant>":
			eat(currentToken);
			break;
		// keywordConstant
		case "<keyword>":
			if(isKeywordConst()) {
				eat(currentToken);
			}
			else {
				throw new java.lang.Error("current keyword " + currentToken + " is not a keywordConstant");
			}
			break;
		// unaryOp
		case "<symbol>":
			if (isUnaryOp()) { // unartOp term
				eat(currentToken);
				compileTerm();
			}
			else if (currentToken.equals("(")) {
				eat("(");
				compileExpression();
				eat(")");
			}
			else {
				throw new java.lang.Error("current symbol " + currentToken + " is not a unaryOp");
			}
			break;
			
		case "<identifier>":
			eatIdentifier(); // varName | subroutine call 

			currentToken = tokens[tokenIndex].split(" ")[1];
			if (currentToken.equals("[")) { // varName (array)
				eat("[");
				compileExpression();
				eat("]");
			}
			else if (currentToken.equals(".")) { // subroutine call
				eat(".");
				eatIdentifier();
				eat("(");
				compileExpressionList();
				eat(")");
			}
			else if (currentToken.equals("(")) { // subroutine call
				eat("(");
				compileExpressionList();
				eat(")");
			}
			break;
		}
		
		outputFileContent += "</term>" + "\n";
		return;
	}
	
	// compile expression list according to Jack grammar
	private void compileExpressionList() {
		outputFileContent += "<expressionList>" + "\n";
		// type varName ?
		currentToken = tokens[tokenIndex].split(" ")[1];
		if (!currentToken.equals(")")) { // "(" not followed by ")"
			compileExpression();
			
			// ("," type varName)*
			while(currentToken.equals(",")) {
				eat(",");
				compileExpression();
			}
		}
		
		outputFileContent += "</expressionList>" + "\n";		
		return;
	}
}
