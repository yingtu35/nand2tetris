import java.util.HashMap;

/** 
 * SubroutineTable.java
 * Represent the subroutine-level symbol table
 * Keep track of local and argument variables in each subroutine of the Jack file
 * @author danie
 *
 */
public class SubroutineTable extends SymbolTable{
	
	private int argCount;
	private int varCount;
	
	public SubroutineTable() {
		return;
	}
	
	public void startSubroutine() {
		typeMap = new HashMap<>();
		kindMap = new HashMap<>();
		indexMap = new HashMap<>();
		argCount = 0;
		varCount = 0;
		return;
	}
	
	public void define(String name, String type, String kind) {
		typeMap.put(name, type);
		kindMap.put(name, kind);
		switch (kind) {
		case "argument":
			indexMap.put(name, argCount);
			argCount++;
			break;
		case "local":
			indexMap.put(name, varCount);
			varCount++;
			break;
		}
		return;
	}
	
	public int varCount(String kind) {
		int count;
		switch (kind) {
		case "argument":
			count = argCount;
			break;
		case "local":
			count = varCount;
			break;
		default:
			System.out.println("Something wrong happens when getting varCount. Please check.");
			count = 0; // should not be executed
			break;
		}
		return count;
	}
}
