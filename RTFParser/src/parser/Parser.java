package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Parser {

	public static void convertRTF(String inputFileName, String outputFileName, RTFConverter converter) {

		RTFReader rtfReader = new RTFReader(converter);
		
		try {
			FileInputStream fis = new FileInputStream(inputFileName);
			rtfReader.parse(new BufferedReader(new InputStreamReader(fis,
					"US-ASCII")));

			List<String> convertedText = converter.getResult();
			if (convertedText != null) {
				System.out.println("******************* Result *******************");
				for (String line : convertedText) {
					System.out.println(line);
				}
				System.out.println("**********************************************");
				writeTextFile(outputFileName, convertedText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Success!");
		}
	}

	private static void writeTextFile(String fileName, List<String> lines)
			throws IOException {
		Path path = Paths.get(fileName);
		
		try (BufferedWriter writer = Files.newBufferedWriter(path,
				StandardCharsets.US_ASCII)) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
		}
	}

}
