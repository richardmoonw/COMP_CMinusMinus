import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Compiler {
    public static void main(String[] argv) {

        // Verify the number of arguments passed to the program.
        if(argv.length != 1){
            System.out.println("You must specify only the filename of your program");
            System.exit(1);
        }

       try {
            String sourceCode = FileManager.readFile(argv[0]);  
            Vector<String> lexemes = new Vector<String>();
            String lexeme = "";
            int state = 0;
            for(int character : sourceCode.toCharArray()) {
                if (state <= 10) {
                    int column = CompilerEnvironment.getColumnNumber(character);
                    state = CompilerEnvironment.TRANSITION_TABLE[state][column];
                    if (column != 18) {
                        lexeme += (char) character;
                    }
                }
                else {
                    lexemes.add(lexeme);
                    lexeme = "";
                    state = 0;
                }
            }

            System.out.println(lexemes);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("The filename you specified does no exist");
            System.exit(1);
        } catch (IOException ioException) {
            System.out.println("There was a problem while reading the file");
            System.exit(1);
        }
    }

}
