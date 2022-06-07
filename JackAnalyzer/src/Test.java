import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
//		String filePath = "D:\\Nand2Tetris\\nand2tetris\\projects\\10\\test.jack";
//		File file = new File(filePath);
//		Scanner sc = new Scanner(file);
//		sc.useDelimiter("");
//		
//		while(sc.hasNext()) {
//			System.out.println(sc.next());
//		}
//		sc.close();
//	
//		String keywordRegex = "[\\p{Punct}&&[^\"_]]";
//		System.out.println("\"".matches(keywordRegex));
//		
//		char chr = '\s';
//		System.out.println(Character.isWhitespace(chr));
		
//		String statementRegex = "(let|if|while|do|return)";
//		String str = "return";
//		System.out.println(str.matches(statementRegex));
		
//		String operatorRegex = "[\\p{Punct}&&[^!~\"#$%&'(),.:;<>?@[\\]^`{}_]]]|(&amp|&lt|&gt)";
//		String str = "~";
//		System.out.println(str.matches(operatorRegex));
		
//		String unaryOpRegex = "[-~]";
//		String str = "+";
//		System.out.println(str.matches(unaryOpRegex));
		
		String example = "<stringConstant> How many numbers?  </stringConstant>";
		String[] strArray = example.split("[<>]");
		for (String i: strArray) {
			System.out.println(i);
		}
		
//		String integerRegex = "-?\\d+";
//		System.out.println("".matches(integerRegex));
//		
//		System.out.println("\"Constant\"".contains("\""));
//		
//		Set<String> keywordSet = Stream.of("class", "constructor", "function", "method", "field", "static",
//				"var", "int", "char", "boolean", "void", "true", "false", "null",
//				"this", "let", "do", "if", "else", "while", "return")
//			.collect(Collectors.toCollection(HashSet::new));
//		String token = "function";
//		System.out.println(keywordSet.contains(token));
	}
//	
	// How to get each token?
	// thought:
	//		first read each line (using while loop and sc.hasNextLine(), clean the line
	//		use split line into array by str.split("\\s+")
	// 		read each element in array, one char at a time
	//		if next char is not a keyword, append it to a temporary string
	//		if next char is a keyword, use commandtype() to determine the type of temporary string (if not a ""), write into xml code
	//			do the same for the keyword
	// 		if no char left, use commandtype() if current temporary string is not ""

}
