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
            // TODO: Implement a better solution for the EOF.
            String sourceCode = FileManager.readFile(argv[0]) + " "; 
            int state = 0;

            Vector<String> lexemes = new Vector<String>();

            int readSourceCode = 0; 
            
            int character = 0;

            
            String lexeme = "";

            while(readSourceCode <= sourceCode.toCharArray().length) {

                while(state <= CompilerEnvironment.FINAL_NORMAL_STATE && readSourceCode <= sourceCode.toCharArray().length) {
                    if(readSourceCode < sourceCode.toCharArray().length) {
                        character = sourceCode.toCharArray()[readSourceCode];
                    }

                    int column = CompilerEnvironment.getColumnNumber(character);

                    state =  CompilerEnvironment.TRANSITION_TABLE[state][column];

                    if(column == CompilerEnvironment.BLANK_COLUMN) {
                        readSourceCode++;
                        continue;
                    }

                    if(state != CompilerEnvironment.ID_TOKEN && state != CompilerEnvironment.NUMBER_TOKEN) {
                        lexeme += (char) character;
                        readSourceCode++;
                    }
                }

                if (state > 10) {
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
