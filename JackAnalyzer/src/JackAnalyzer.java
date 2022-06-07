
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/** 
 * Jack Analyzer read the input (a jack filr or a jack file directory)
 * Produce the tokenized XML code of the given input
 * @author danie
 *
 */
public class JackAnalyzer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		CompilationEngine parser = new CompilationEngine();
		
		
		JackTokenizer jackTokenizer;
		// Determine whether the argument is a file or a folder
		if(args[0].contains(".jack")){ // file
			
			jackTokenizer = new JackTokenizer(args[0]);
			jackTokenizer.run();
		}
		else { // folder

			// get each file
			List<String> fileList = Files.list(Paths.get(args[0]))
		        	.filter(Files::isRegularFile)
//				        	.map(Path::getFileName)
		        	.map(Path::toString)
		        	.filter(str -> str.contains(".jack"))
		        	.collect(Collectors.toList());
			
			for(String file: fileList) {
//				System.out.println(file);
				
				jackTokenizer = new JackTokenizer(file);
				jackTokenizer.run();
			}
		}
	}

}
