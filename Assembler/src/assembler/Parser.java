package assembler;

public class Parser {
	
	private SymbolTable symbolTable;
	
	public Parser(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}
	// remove in-line comment and whitespace
	public String cleanInlineCom(String str) {
		if(str.contains("//")) {
			String[] strSplit = str.split("//");
			str = strSplit[0].replaceAll("\\s", "");
			}else {
				str = str.replaceAll("\\s", "");
			}
		return str;
		}
	
	// add label to the SymbolTable 
	public void addLabel(String str, int n) {
		String label = str.substring(1, str.length()-1); // remove parenthesis
//		System.out.println(label);
		symbolTable.addLabel(label, n);
		}
	
	// helper function to determine if value of a instruction is numeric
	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int number = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
//	    	System.out.println(strNum + " cannot be parseInt.");
	        return false;
	    }
	    return true;
	}
	
	// get value of A instruction (no @)
	public String aInstruction(String str) {
		String newStr = str.substring(1);
//		System.out.println(newStr);
		boolean check = !symbolTable.hasSymbol(newStr) && !isNumeric(newStr);
//		System.out.println(check);
		if(check) {
			int memLoc = symbolTable.getMemLoc();
			symbolTable.addVariable(newStr, memLoc);
//			System.out.println(newStr + " put into symbolTable");
		}
		return newStr;
	}
	
	// get dest of C instruction
	public String dest(String str) {
		if(str.contains("=")) {
			return str.split("=")[0];
		}
		else {
			return "null";
		}
		
	}
	
	// get comp of C instruction
	public String comp(String str) {
		if(str.contains("=") && str.contains(";")){
			return str.split("=")[1].split(";")[0];
		}
		else if(str.contains("=")) {
			return str.split("=")[1];
		}
		else {
			return str.split(";")[0];
		}
	}
	
	// get jump of C instruction
	public String jump(String str) {
		if(str.contains(";")) {
			return str.split(";")[1];
		}
		else {
			return "null";
		}
	}
}
