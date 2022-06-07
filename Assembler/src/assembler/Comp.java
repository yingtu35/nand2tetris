package assembler;

public class Comp {
	
	private SymbolTable symbolTable;
	
	public Comp(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}
	
	// get binary code of A value
	public String value(String str) {
//		System.out.println(str);
		try{
            int number_10 = Integer.parseInt(str);
            String number = Integer.toBinaryString(number_10);
//            System.out.println(number); // output = 25
            return number;
        }
        catch (NumberFormatException ex){ // value is a variable
//            ex.printStackTrace();
//        	System.out.println(symbolTable.getSymbol(str));
        	int addr = Integer.parseInt(symbolTable.getSymbol(str));
        	String number = Integer.toBinaryString(addr);
        	return number;
        }
	}
	
	// get binary code of C dest
	public String dest(String str) {
		return symbolTable.getDest(str);
	}
	
	// get binary code of C comp
	public String comp(String str) {
		return symbolTable.getComp(str);
	}
	
	// get binary code of C jump
	public String jump(String str) {
		return symbolTable.getJump(str);
	}
	
	
}
