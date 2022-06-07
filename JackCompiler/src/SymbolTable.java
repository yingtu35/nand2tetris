import java.util.Map;

/** 
 * SymbolTable.java
 * Represent the abstract symbol table that should only be inherited by other subclasses
 * Cannot be instantiated
 * provide methods to check and get access to type, kind and index of symbols
 * @author danie
 *
 */
abstract class SymbolTable {
	
	protected Map<String, String> typeMap;
	protected Map<String, String> kindMap;
	protected Map<String, Integer> indexMap;
	
	// reset the subroutine's symbol table
	public abstract void startSubroutine();
	
	// put new symbol into symbol table
	public abstract void define(String name, String type, String kind);
	
	// get the number of symbols of a given kind in the symbol table
	public abstract int varCount(String kind);
	
	// get the kind of the name symbol
	public String kindOf(String name) {
		return kindMap.get(name);
	}
	
	// get the type of the name symbol
	public String typeOf(String name) {
		return typeMap.get(name);
	}
	
	// get the index of the name symbol
	public int indexOf(String name) {
		return indexMap.get(name);
	}
	
	// return true if the symbol is in the table
	public boolean hasKey(String name) {
		return kindMap.containsKey(name);
	}
	
	
	
	
	

}
