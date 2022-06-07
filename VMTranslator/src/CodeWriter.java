
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
	
	private String fileContent = "";
	private String fileName;
	private String outputFile;
	private Boolean isMultipleFiles;
	private int eqCount = 0;
	private int gtCount = 0;
	private int ltCount = 0;
	
	public CodeWriter(String outputFile, Boolean isMultipleFiles) {
		this.outputFile = outputFile;
		this.isMultipleFiles = isMultipleFiles;
	}
	
	public CodeWriter(String fileName, String outputFile, Boolean isMultipleFiles) {
		this.fileName = fileName;
		this.outputFile = outputFile;
		this.isMultipleFiles = isMultipleFiles;
	}
	
	public void writeArithmetic(String str) {
		switch(str) {
			case "add":
				add();
				break;
			case "sub":
				sub();
				break;
			case "neg":
				neg();
				break;
			case "eq":
				eq();
				break;
			case "gt":
				gt();
				break;
			case "lt":
				lt();
				break;
			case "and":
				and();
				break;
			case "or":
				or();
				break;
			case "not":
				not();
				break;
		}
	}
	
	private void writeCode(String[] codes) {
		for (String code: codes) {
			// System.out.println(code);
			fileContent = fileContent.concat(code + "\n");
		}
	}
	
	public void writeInit() {
		String[] codes = {String.format("// Bootstrap code"),
				"@256", "D=A", "@SP", "M=D"}; // SP = 256
		writeCode(codes);
		writeCall("Sys.init", "0", 0);		
	}
	
	public void writePush(String functionName, String segment, String idx) {
		if("static".equals(segment)) {
			String[] codes = {String.format("// push static %s", idx), String.format("@%s.%s", functionName, idx), "D=M",
					"@SP", "AM=M+1", "A=A-1", "M=D"};
			writeCode(codes);
		}
		else if("constant".equals(segment)) {
			String[] codes = {String.format("// push constant %s", idx), String.format("@%s", idx), "D=A",
					"@SP", "AM=M+1", "A=A-1", "M=D"};
			writeCode(codes);
		}
		else if("temp".equals(segment)) {
			String[] codes = {String.format("// push temp %s", idx), String.format("@%s", Integer.toString(5+Integer.parseInt(idx))), "D=M",
					"@SP", "AM=M+1", "A=A-1", "M=D"};
			writeCode(codes);
		}
		else if("pointer".equals(segment)) {
			String[] codes = {String.format("// push pointer %s", idx), String.format("@%s", Integer.toString(3+Integer.parseInt(idx))), "D=M",
					"@SP", "AM=M+1", "A=A-1", "M=D"};
			writeCode(codes);
		}
		else {
			String[] codes = {String.format("// push %s %s", segment, idx), String.format("@%s", segment), "D=M", "@" + idx, "A=D+A", "D=M",
					"@SP", "AM=M+1", "A=A-1", "M=D"};
			writeCode(codes);
		}	
	}
	
	public void writePop(String functionName, String segment, String idx) {
		if("static".equals(segment)) {
			String[] codes = {String.format("// pop static %s", idx), "@SP", "AM=M-1", "D=M", String.format("@%s.%s", functionName, idx), "M=D"};
			writeCode(codes);
		}
		else if("temp".equals(segment)) {
			String[] codes = {String.format("// pop temp %s", idx), String.format("@%s", Integer.toString(5+Integer.parseInt(idx))), "D=A",
					"@R13", "M=D", "@SP", "AM=M-1", "D=M", "@R13", "A=M", "M=D"};
			writeCode(codes);
		}
		else if("pointer".equals(segment)) {
			String[] codes = {String.format("// pop pointer %s", idx), "@SP", "AM=M-1", "D=M", String.format("@%s", Integer.toString(3+Integer.parseInt(idx))), "M=D"};
			writeCode(codes);
		}
		else {
			String[] codes = {String.format("// pop %s %s", segment, idx), String.format("@%s", segment), "D=M", "@" + idx, "D=D+A",
					"@R13", "M=D", "@SP", "AM=M-1", "D=M", "@R13", "A=M", "M=D"};
			writeCode(codes);
		}
	}
	
	// given a string of label declaration, construct the assembly language and write it into the asm file
	public void writeLabel(String label) {
		String[] codes = {String.format("// declare label %s", label), "(" + label + ")"};
		writeCode(codes);
	}
	
	// given a string of label destination, construct the assembly language and write it into the asm file
	public void writeGoto(String label) {
		String[] codes = {String.format("// GOTO label %s", label), "@"+label, "0;JMP"};
		writeCode(codes);
	}
	
	// given a string of label destination, construct the assembly language and write it into the asm file
	public void writeIf(String label) {
		String[] codes = {String.format("// IF-GOTO label %s", label), "@SP", "AM=M-1", "D=M", "@"+label,"D;JNE"};
		writeCode(codes);
	}
	
	// given strings functionName and nArgs, construct the assembly language and write it into the asm file
	public void writeFunction(String functionName, String nVars) {
		String[] funcArray = functionName.split("\\.");
		String function = funcArray[0];
		String label = funcArray[1];
		if (this.isMultipleFiles) {
			String[] codes = {String.format("// Define Function %s in Function %s in file %s", label, function, this.fileName),
							"("+this.fileName+"."+function+"$"+label+")",
							"@"+nVars, "D=A", "@R13", "M=D", "@R14", "M=0", // declare i & n
							"("+this.fileName+"."+function+"$"+label+"LOOP)", "@R14", "D=M", "@R13", "D=D-M", // D = i-n
							"@"+this.fileName+"."+function+"$"+label+"LOOPEND", "D;JEQ", // check condition
							"@SP", "AM=M+1", "A=A-1", "M=0", // push constant 0
							"@R14", "M=M+1", "@"+this.fileName+"."+function+"$"+label+"LOOP", "0;JMP", // i++, goto LOOP
							"("+this.fileName+"."+function+"$"+label+"LOOPEND)"}; // function initial loop end
			writeCode(codes);
		}
		else {
			String[] codes = {String.format("// Define Function %s", label), "("+label+")",
							"@"+nVars, "D=A", "@R13", "M=D", "@R14", "M=0", // declare i & n
							"("+label+"LOOP)", "@R14", "D=M", "@R13", "D=D-M", "@"+label+"LOOPEND", "D;JEQ", // check condition
							"@SP", "AM=M+1", "A=A-1", "M=0", // push constant 0
							"@R14", "M=M+1", "@"+label+"LOOP", "0;JMP", // i++, goto LOOP
							"("+label+"LOOPEND)"}; // function initial loop end
			writeCode(codes);
		}
	}
	
	// given strings functionName and nVars, construct the assembly language and write it into the asm file
	// !!!
	public void writeCall(String functionName, String nArgs, int callCount) {
		String[] funcArray = functionName.split("\\.");
		String function = funcArray[0];
		String label = funcArray[1];
		if (this.isMultipleFiles) {
			String[] codes = {String.format("// Call Function %s in Function %s in file %s", label, function, this.fileName),
							"@"+this.fileName+"."+function+"$ret."+Integer.toString(callCount), "D=A", "@SP", "AM=M+1", "A=A-1", "M=D", // push return address
							"@LCL", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",   // push LCL
							"@ARG", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",   // push ARG
							"@THIS", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",  // push THIS
							"@THAT", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",  // push THAT
							"@"+nArgs, "D=A", "@5", "D=D+A", "@SP", "D=M-D", "@ARG", "M=D", // ARG = SP - 5 - nArgs
							"@SP", "D=M", "@LCL", "M=D", // LCL = SP
							"@"+this.fileName+"."+function+"$"+label, "0;JMP",   // goto label 
							"("+this.fileName+"."+function+"$ret."+Integer.toString(callCount)+")"};  // (return address)  
			writeCode(codes);
		}
		else {
			String[] codes = {String.format("// Call Function %s", label),
							"@"+"$ret."+Integer.toString(callCount), "D=A", "@SP", "AM=M+1", "A=A-1", "M=D", // push return address
							"@LCL", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",   // push LCL
							"@ARG", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",   // push ARG
							"@THIS", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",  // push THIS
							"@THAT", "D=M", "@SP", "AM=M+1", "A=A-1", "M=D",  // push THAT
							"@"+nArgs, "D=A", "@5", "D=D+A", "@SP", "D=M-D", "@ARG", "M=D", // ARG = SP - 5 - nArgs
							"@SP", "D=M", "@LCL", "M=D", // LCL = SP
							"@"+label, "0;JMP",   // goto label 
							"("+"$ret."+Integer.toString(callCount)+")"};  // (return address)  
			writeCode(codes);
		}
	}
	
	// construct the assembly language and write it into the asm file
	public void writeReturn() {
		String[] codes = {"// return", "@LCL", "D=M", "@R13 // endFrame", "M=D",  // endFrame = LCL
						"@R13", "D=M", "@5", "A=D-A", "D=M", "@R14 // retaddr", "M=D", // retaddr = *(endFrame-5)
						"@ARG", "D=M", "@R15", "M=D", "@SP", "AM=M-1", "D=M", "@R15", "A=M", "M=D", // *ARG = pop()
						"@ARG", "D=M", "@SP", "M=D+1", // SP = ARG+1
						"@R13 // endFrame", "D=M", "@1", "A=D-A", "D=M", "@THAT", "M=D", // THAT = *(endFrame-1)
						"@R13 // endFrame", "D=M", "@2", "A=D-A", "D=M", "@THIS", "M=D", // THIS = *(endFrame-2)
						"@R13 // endFrame", "D=M", "@3", "A=D-A", "D=M", "@ARG", "M=D",  //  ARG = *(endFrame-3)
						"@R13 // endFrame", "D=M", "@4", "A=D-A", "D=M", "@LCL", "M=D", //   LCL = *(endFrame-4)
						"@R14", "A=M", "0;JMP"}; // goto retaddr
		writeCode(codes);
	}
	
	
	private void add() {
		String[] codes = {"// add", "@SP", "AM=M-1", "D=M", "A=A-1", "M=D+M"};
		writeCode(codes);
	}

	
	private void sub() {
		String[] codes = {"// sub", "@SP", "AM=M-1", "D=M", "A=A-1", "M=M-D"};
		writeCode(codes);
	}
	
	private void neg() {
		String[] codes = {"// neg", "@SP", "A=M-1", "M=-M"};
		writeCode(codes);
	}
	
	private void eq() {
		this.eqCount += 1;
		String eq = Integer.toString(eqCount);
		String[] codes = {"// eq", "@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D", String.format("@EQ%s", eq), "D;JEQ", "D=1",
				String.format("(EQ%s)", eq), "@SP", "A=M-1", "M=D-1"};
		writeCode(codes);
	}
	
	private void gt() {
		this.gtCount += 1;
		String gt = Integer.toString(gtCount);
		String[] codes = {"// gt", "@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D", String.format("@GT%s", gt), "D;JGT", "D=1", String.format("@GTDONE%s", gt), "0;JMP",
				String.format("(GT%s)", gt), "D=0", String.format("(GTDONE%s)", gt),  "@SP", "A=M-1", "M=D-1"};
		writeCode(codes);
	}
	
	private void lt() {
		this.ltCount += 1;
		String lt = Integer.toString(ltCount);
		String[] codes = {"// lt", "@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D", String.format("@LT%s", lt), "D;JLT", "D=1", String.format("@LTDONE%s", lt), "0;JMP",
				String.format("(LT%s)", lt), "D=0", String.format("(LTDONE%s)", lt), "@SP", "A=M-1", "M=D-1"};
		writeCode(codes);
	}
	
	private void and() {
		String[] codes = {"// and", "@SP", "AM=M-1", "D=M", "A=A-1", "M=D&M"};
		writeCode(codes);
	}
	
	private void or() {
		String[] codes = {"// or", "@SP", "AM=M-1", "D=M", "A=A-1", "M=D|M"};
		writeCode(codes);
	}
	
	private void not() {
		String[] codes = {"// not", "@SP", "A=M-1", "M=!M"};
		writeCode(codes);
	}
	
	public void close() throws IOException { 
		FileWriter writer = new FileWriter(this.outputFile);
		writer.write(fileContent);
		writer.close();
	}
}