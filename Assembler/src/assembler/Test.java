package assembler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Test {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// the file to be opened for reading
		SymbolTable symbolTable = new SymbolTable();
		Parser parser = new Parser(symbolTable);
		try {
			FileInputStream fis = new FileInputStream("src/assembler/Demo.txt");

			Scanner sc = new Scanner(fis); // file to be scanned
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
//				System.out.println(line);
				if(line.equals("") || line.substring(0,2).contains("//")) { // comment line
					continue;
				}
				else{
					line = parser.cleanInlineCom(line);
				}
				System.out.println(line);
//				if(line.contains("//")) { // in-line comment
//					String[] lineSplit = line.split("//");
//					line = lineSplit[0];
//				}
//				
//				if(line.contains("(")) { 
//					line = line.replaceAll("\\s", "");
//					System.out.println(line.substring(1, line.length()-1));
//				}
//				else if(line.contains("@")) { // A instruction
//					line = line.replaceAll("\\s", "");
//					System.out.println(line.substring(1));
//					}
//				else {
//					String dest;
//					String comp;
//					String jump;
//					line = line.replaceAll("\\s", ""); // remove all whitespace
//					String[] cInsSplit = line.split("=");
//					dest = cInsSplit[0];
//					if(cInsSplit[1].contains(";")) {
//						String[] compOrJump = cInsSplit[1].split(";"); // split comp and jump
//						comp = compOrJump[0];
//						jump = compOrJump[1];
//					}else {
//						comp = cInsSplit[1];
//						jump = "";
//					}
//					System.out.println(dest + " " + comp + " " + jump);
//				}
			}
			sc.close();
			FileInputStream fis2 = new FileInputStream("src/assembler/Demo.txt");
			Scanner sc2 = new Scanner(fis2); // file to be scanned
			while(sc2.hasNextLine()) {
				String line = sc2.nextLine();
				System.out.println(line);
			}
//			int number = 25;
//			String binary = Integer.toBinaryString(number);
//			binary = String.format("%16s", binary).replace(' ', '0');
//			System.out.println(binary);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}
