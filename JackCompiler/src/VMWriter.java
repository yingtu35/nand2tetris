import java.io.FileWriter;
import java.io.IOException;

/**
 * VMwriter.java
 * represent a VM code writer that produce VM code
 * Instantiated by the CompilationEngine class 
 * subroutine is provoked from CompilationEngine class to conduct VM code writing
 * @author danie
 *
 */
public class VMWriter {
	
	private String outputFileContent = "";
	private String outputFilePath;
	
	// given a path of output file, construct a VMWriter
	public VMWriter(String outputFilePath) {
		this.outputFilePath = outputFilePath;
		return;
	}
	
	// write VM code "push segment i"
	public void writePush(String segment, int i) {
		this.outputFileContent = this.outputFileContent + "push " + segment + " " + i + "\n";
		return;
	}
	
	// write VM code "pop segment i"
	public void writePop(String segment, int i) {
		this.outputFileContent = this.outputFileContent + "pop " + segment + " " + i + "\n";
		return;
	}
	
	// write VM arithmetic command
	public void writeArithmetic(String command) {
		this.outputFileContent = this.outputFileContent + command + "\n";
		return;
	}
	
	// write VM label
	public void writeLabel(String label) {
		this.outputFileContent = this.outputFileContent + "label " + label + "\n";
		return;
	}
	
	// write VM goto command
	public void writeGoto(String label) {
		this.outputFileContent = this.outputFileContent + "goto " + label + "\n";
		return;
	}
	
	// write VM if-goto command
	public void writeIf(String label) {
		this.outputFileContent = this.outputFileContent + "if-goto " + label + "\n";
		return;
	}
	
	// write VM function call command "call name nArgs"
	public void writeCall(String name, int nArgs) {
		this.outputFileContent = this.outputFileContent + "call " + name + " " + nArgs + "\n";
		return;
	}
	
	// write VM function declaration command "function name nVars"
	public void writeFunction(String name, int nLocals) {
		this.outputFileContent = this.outputFileContent + "function " + name + " " + nLocals + "\n";
		return;
	}
	
	// write VM return command
	public void writeReturn() {
		this.outputFileContent = this.outputFileContent + "return" + "\n";
		return;
	}
	
	// output the file to the path given when constructing, called when VM code writing is done
	public void close() throws IOException{
		FileWriter writer = new FileWriter(this.outputFilePath);
		writer.write(outputFileContent);
		writer.close();
		return;
	}
}
