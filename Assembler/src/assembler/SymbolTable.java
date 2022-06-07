package assembler;

import java.util.HashMap;
import java.util.Map;
public class SymbolTable {
	private Map<String, String> symbolTable = new HashMap<String, String>();
	private Map<String, String> compTable = new HashMap<String, String>();
	private Map<String, String> destTable = new HashMap<String, String>();
	private Map<String, String> jumpTable = new HashMap<String, String>();
	private static int memLoc = 16;
	
	public SymbolTable() {
		initialize();
	}
	public void addLabel(String symbol, int address) {
		String addr = Integer.toString(address);
		this.symbolTable.put(symbol, addr);
	}
	
	public void addVariable(String variable, int address) {
		String addr = Integer.toString(address);
		this.symbolTable.put(variable, addr);
		memLoc++;
	}
	
	public int getMemLoc() {
		return memLoc;
	}
	public String getSymbol(String symbol) {
		return this.symbolTable.get(symbol);
	}
	
	public boolean hasSymbol(String symbol) {
		return this.symbolTable.containsKey(symbol);
	}
	
	public String getComp(String symbol) {
		return this.compTable.get(symbol);
	}
	
	public String getDest(String symbol) {
		return this.destTable.get(symbol);
	}
	
	public String getJump(String symbol) {
		return this.jumpTable.get(symbol);
	}
	
	private void initialize() {
		// add predefined symbol
		this.symbolTable.put("R0", "0");this.symbolTable.put("R12", "12");
		this.symbolTable.put("R1", "1");this.symbolTable.put("R13", "13");
		this.symbolTable.put("R2", "2");this.symbolTable.put("R14", "14");
		this.symbolTable.put("R3", "3");this.symbolTable.put("R15", "15");
		this.symbolTable.put("R4", "4");this.symbolTable.put("SCREEN", "16384");
		this.symbolTable.put("R5", "5");this.symbolTable.put("KBD", "24576");
		this.symbolTable.put("R6", "6");this.symbolTable.put("SP", "0");
		this.symbolTable.put("R7", "7");this.symbolTable.put("LCL", "1");
		this.symbolTable.put("R8", "8");this.symbolTable.put("ARG", "2");
		this.symbolTable.put("R9", "9");this.symbolTable.put("THIS", "3");
		this.symbolTable.put("R10", "10");this.symbolTable.put("THAT", "4");
		this.symbolTable.put("R11", "11");
		
		// add C comp symbols
		this.compTable.put("0", "0101010");this.compTable.put("1", "0111111");this.compTable.put("-1", "0111010");
		this.compTable.put("D", "0001100");this.compTable.put("A", "0110000");this.compTable.put("!D", "0001101");
		this.compTable.put("!A", "0110001");this.compTable.put("-D", "0001111");this.compTable.put("-A", "0110011");
		this.compTable.put("D+1", "0011111");this.compTable.put("A+1", "0110111");this.compTable.put("D-1", "0001110");
		this.compTable.put("A-1", "0110010");this.compTable.put("D+A", "0000010");this.compTable.put("D-A", "0010011");
		this.compTable.put("A-D", "0000111");this.compTable.put("D&A", "0000000");this.compTable.put("D|A", "0010101");
		this.compTable.put("M", "1110000");this.compTable.put("!M", "1110001");this.compTable.put("-M", "1110011");
		this.compTable.put("M+1", "1110111");this.compTable.put("M-1", "1110010");this.compTable.put("D+M", "1000010");
		this.compTable.put("D-M", "1010011");this.compTable.put("M-D", "1000111");this.compTable.put("D&M", "1000000");
		this.compTable.put("D|M", "1010101");
		
		// add C dest symbols
		this.destTable.put("null", "000");this.destTable.put("M", "001");
		this.destTable.put("D", "010");this.destTable.put("MD", "011");
		this.destTable.put("A", "100");this.destTable.put("AM", "101");
		this.destTable.put("AD", "110");this.destTable.put("AMD", "111");
		
		// ADD C jump symbols
		this.jumpTable.put("null", "000");this.jumpTable.put("JGT", "001");
		this.jumpTable.put("JEQ", "010");this.jumpTable.put("JGE", "011");
		this.jumpTable.put("JLT", "100");this.jumpTable.put("JNE", "101");
		this.jumpTable.put("JLE", "110");this.jumpTable.put("JMP", "111");
	}
}
