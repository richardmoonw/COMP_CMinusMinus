public class FileManagerr {
	
	public static String readFile(String filename) throws FileNotFoundException, IOException {
		
		String sourceCode = "";
		
		try {
			FileInputStream sourceFile = new FileInputStream(filename);
			int data = sourceFile.read();

			while (data != -1) {
				sourceCode += (char) data;
				data = sourceFile.read();

			}
		
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
