import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VMTranslator {
	
	// read input file and pass file to Parser for parsing
	// parameter: file name (String)
	// return : void
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Determine whether the argument is a file or a folder
		if(args[0].contains(".vm")){ // file

			// get file name
//			String[] argArray = args[0].split("\\W+"); // split based on non-word character
//			String fileName = argArray[argArray.length-2];
			String outputFile = args[0].split("\\.")[0] + ".asm";
			
			CodeWriter codeWriter = new CodeWriter(outputFile, false);
			Parser parser = new Parser(codeWriter);
			
			parser.parse(args[0]);
			
			codeWriter.close();
		}
		else { // folder
			// get folder name and outputFile name
			String[] argArray = args[0].split("\\W");
			String folderName = argArray[argArray.length-1];
			String outputFile = args[0] + "\\" + folderName + ".asm";
			
			// get each file
			List<String> fileList = Files.list(Paths.get(args[0]))
		        	.filter(Files::isRegularFile)
//		        	.map(Path::getFileName)
		        	.map(Path::toString)
		        	.filter(str -> str.contains(".vm"))
		        	.collect(Collectors.toList());
			
			CodeWriter codeWriter = new CodeWriter(folderName, outputFile, true);
			// Write Bootstrap code
			codeWriter.writeInit();
			
			Parser parser = new Parser(codeWriter);
			
			for(String file: fileList) {
//				System.out.println(file);
				parser.parse(file);
			}
			codeWriter.close();
		}
		
		
		// split based on non-word character
//		String[] argArray = args[0].split("\\W+");
//		for(String i: argArray) {
//			System.out.println(i);
//		}
		
//		String fileContent = "";
//		while(sc.hasNextLine()) {
//			String line = sc.nextLine();
//			System.out.println(line);
//			fileContent = fileContent.concat(line + "\n");
//		}
//		sc.close();
//		
//		FileWriter writer = new FileWriter("Test.txt");
//		writer.write(fileContent);
//		writer.close();
//		
	}

}
