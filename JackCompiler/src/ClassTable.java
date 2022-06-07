import java.util.HashMap;

/** 
 * ClassTable.java
 * Represent the class-level symbol table
 * Keep track of field and static variables in the Jack file
 * @author danie
 *
 */
public class ClassTable extends SymbolTable{

	private int staticCount;
	private int thisCount;
	
	public ClassTable() {
		return;
	}
	
	public void startSubroutine(){
		typeMap = new HashMap<>();
		kindMap = new HashMap<>();
		indexMap = new HashMap<>();
		staticCount = 0;
		thisCount = 0;
		return;
	}
	
	public void define(String name, String type, String kind) {
		typeMap.put(name, type);
		kindMap.put(name, kind);
		switch (kind) {
		case "static":
			indexMap.put(name, staticCount);
			staticCount++;
			break;
		case "this":
			indexMap.put(name, thisCount);
			thisCount++;
			break;
		}
		return;
	}
	
	public int varCount(String kind) {
		int count;
		switch (kind) {
		case "static":
			count = staticCount;
			break;
		case "this":
			count = thisCount;
			break;
		default:
			System.out.println("Something wrong happens when getting varCount. Please check.");
			count = 0; // should not be executed
			break;
		}
		return count;
	}
	
}
