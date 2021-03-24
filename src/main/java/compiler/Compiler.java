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

            Vector<Object[]> lexemes = new Vector<Object[]>();

            int readSourceCode = 0; 
            
            int character = 0;

            
            String lexeme = "";

            while(readSourceCode <= sourceCode.toCharArray().length) {

                while(state < CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE && readSourceCode <= sourceCode.toCharArray().length) {
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

                if (state >= CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE && state < CompilerEnvironment.FIRST_STATE_OF_ERROR) {
                    if (state != CompilerEnvironment.COMMENT_TOKEN) {
                        if (state == CompilerEnvironment.ID_TOKEN) {
                            if (CompilerEnvironment.isKeyword(lexeme)) {
                                lexemes.add(new Object[] {lexeme});
                            } else {
                                CompilerEnvironment.setIdentifierSymbolTable(lexeme);
                                Object[] new_identifier = new Object[] {"id", CompilerEnvironment.getIdentifierSymbolTableIndex() };
                                lexemes.add(new_identifier);
                            }
                        }
                        else if (state == CompilerEnvironment.NUMBER_TOKEN) {
                            CompilerEnvironment.setNumberSymbolTable(Integer.parseInt(lexeme));
                            Object[] new_identifier = new Object[] {"num", CompilerEnvironment.getNumberSymbolTableIndex() };
                            lexemes.add(new_identifier);
                        }
                        else {
                            lexemes.add(new Object[] {lexeme});
                        }
                        
                    }
                    lexeme = "";
                    state = 0;
                }
            }
            
            for(Object[] token : lexemes) {
                if (token.length == 1) {
                    System.out.println(token[0]);
                } else {
                    System.out.println(token[0] + " " + token[1]);
                }
                
            }

            System.out.println(CompilerEnvironment.getIdentifierSymbolTable());
            System.out.println(CompilerEnvironment.getNumberSymbolTable());
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("The filename you specified does no exist");
            System.exit(1);
        } catch (IOException ioException) {
            System.out.println("There was a problem while reading the file");
            System.exit(1);
        }
    }

}
