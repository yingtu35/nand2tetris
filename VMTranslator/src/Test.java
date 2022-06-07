import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		try {
//			Files.list(Paths.get(args[0]))
//	          .filter(Files::isRegularFile)
//	          .forEach(System.out::println);
//	          .map(Path::getFileName)
//	          .map(Path::toString)
//	          .collect(Collectors.toSet());
//	    }
		
		// collect all VM files in a folder to a list
		String[] argArray = args[0].split("\\W");
		String folderName = argArray[argArray.length-1];
		List<String> fileList = Files.list(Paths.get(args[0]))
							        	.filter(Files::isRegularFile)
//							        	.map(Path::getFileName)
							        	.map(Path::toString)
							        	.filter(str -> str.contains(".vm"))
//							        	.map(str -> str.split("\\.")[0])
							        	.collect(Collectors.toList());
		System.out.println(folderName);
		for(String file: fileList) {
			System.out.println(file);
		}
//							        	.forEach(System.out::println);
		
//		File file = new File(args[0]);
//		Scanner sc = new Scanner(file);
//		
//		// Pattern.quote for dealing with special character 
//		// split based on non-word character
//		String[] argArray = args[0].split("\\W+");
//		String fileName = argArray[argArray.length-2];
//		String outputFile = args[0].split("\\.")[0] + ".asm";
//		
//		Parser parser = new Parser(sc, fileName, outputFile);
//		parser.run();
	}

}
