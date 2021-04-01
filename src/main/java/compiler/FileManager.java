import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManager {
	
	public static String readFile(String filename) throws FileNotFoundException, IOException {
		
		// Declare a variable to store the content of the file.
		String sourceCode = "";
		
		try {
			// Open the specified file as a stream of bytes.
			FileInputStream sourceFile = new FileInputStream(filename);
			
			// Read the content of the file until the EOF is found.
			int data = sourceFile.read();
			while (data != -1) {
				sourceCode += (char) data;
				data = sourceFile.read();
			}
			
			// Close the file.
			sourceFile.close();
		}

		catch (FileNotFoundException fileNotFoundException) {
			throw fileNotFoundException;
		}
		catch (IOException ioException) {
			throw ioException;
		}
	
		return sourceCode;
	}
}
