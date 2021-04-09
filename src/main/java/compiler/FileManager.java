/* 
The FileManager class is in charge of reading the source code to analyze from a given file. It just stores
all the content of the file in a String variable that will be used later by the Compiler class in order to 
perform the lexical analysis through all its elements. In case that any error occurs while opening or reading 
the file, the method should throw an error indicating the failure to the main flow of the system.
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileManager {
	
	// Method used to read the source code from a specified file, store it in a String variable and return that
	// variable.
	public static String readFile(String filename) throws FileNotFoundException, IOException {
		
		// Declare a variable to store the source code contained in the file.
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

		// If the specified file does not exist, throw an exception indicating it.
		catch (FileNotFoundException fileNotFoundException) {
			throw fileNotFoundException;
		}
		// If there was a problem reading the content of the file, throw an exception indicating it.
		catch (IOException ioException) {
			throw ioException;
		}
	
		return sourceCode;
	}
}
