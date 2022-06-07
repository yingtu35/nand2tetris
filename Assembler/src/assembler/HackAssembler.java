package assembler;

import java.io.*;
import java.util.Scanner;
public class HackAssembler {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SymbolTable symbolTable = new SymbolTable();
		Parser parser = new Parser(symbolTable);
		Comp comp = new Comp(symbolTable);
		int lineCount = 0;
		try {
			// the file to be opened for reading
			FileInputStream fis = new FileInputStream("src/Add.asm");
			Scanner sc = new Scanner(fis); // file to be scanned
			
			// the file to be written
			File fout = new File("src/Add.hack");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			// first pass
			// find labels
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.equals("") || line.substring(0,2).contains("//")) { // whitespace line or comment line
					continue;
				}else {
					line = parser.cleanInlineCom(line);
				}
				
				if(line.contains("(")) {
					parser.addLabel(line, lineCount);
				}else {
					lineCount++;
				}
			}
			lineCount = 0; // reset lineCount
			
			sc.close();
			FileInputStream fis2 = new FileInputStream("src/Add.asm");
			Scanner sc2 = new Scanner(fis2);
			
			// second pass
			// convert assembly code to machine binary code
			while(sc2.hasNextLine()) {
				String line = sc2.nextLine();
				
				if(line.equals("") || line.substring(0,2).contains("//")) { // whitespace line or comment line
					continue;
				}else {
					line = parser.cleanInlineCom(line);
				}
				
				if(line.contains("(")) { // label already been processed
					continue;
				}
				String binary;
				if(line.contains("@")) { // A instruction
					String value = parser.aInstruction(line);
					binary = String.format("%16s", comp.value(value)).replace(' ', '0');
				}
				else { // C instruction
					String d = parser.dest(line);
					String c = parser.comp(line);
					String j = parser.jump(line);
					
					String destBinary = comp.dest(d);
					String compBinary = comp.comp(c);
					String jumpBinary = comp.jump(j);
					binary = "111" + compBinary + destBinary + jumpBinary;
				}
//				System.out.println(original + "-> " + binary);
				bw.write(binary);
				bw.newLine();
			}
			sc2.close();
			bw.close(); // close the buffered writer
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
