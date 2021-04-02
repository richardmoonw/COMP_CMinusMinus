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
            // Read the content of the specified file and store it in a String variable.
            String sourceCode = FileManager.readFile(argv[0]) + " "; 

            // Define the variables that will help while iterating over the source code. 
            int state = 0;
            int readSourceCode = 0; 
            int character = 0;
            String lexeme = "";
            int column = 0;

            // Declare a vector to store the token sequence produced by the lexical analyzer.
            Vector<Object[]> sequenceOfTokens = new Vector<Object[]>();

            
            // Read characters from the sourceCode until the end of the string is found.
            while(readSourceCode <= sourceCode.toCharArray().length) {

                // While the state is not an acceptance or an error state.
                while(state < CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE && readSourceCode <= sourceCode.toCharArray().length) {

                    // If there are still characters to be read in the sourceCode, read the next character.
                    if(readSourceCode < sourceCode.toCharArray().length) {
                        character = sourceCode.toCharArray()[readSourceCode];
                    }

                    // Get the column's index value of the transition table given the ASCII code of the character read.
                    column = CompilerEnvironment.getColumnNumber(character);

                    // Calculate the new state with the current one and the transition given by the column value.
                    state =  CompilerEnvironment.TRANSITION_TABLE[state][column];
                    
                    // If the character read is a blank, just ignore it.
                    if(column == CompilerEnvironment.BLANK_COLUMN) {
                        readSourceCode++;
                        continue;
                    }

                    // If the new state is not an state of acceptance, add the character to the current lexeme and continue
                    // to the next iteration.
                    if(state < CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE) {
                        lexeme += (char) character;
                        readSourceCode++;
                    }
                }

                // If the new state is an acceptance state.
                if (state >= CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE && state < CompilerEnvironment.FIRST_STATE_OF_ERROR) {
                    Object[] token = new Object[2];

                    // If the new state corresponds to an identifier token.
                    if (state == CompilerEnvironment.ID_TOKEN) {
                        
                        // If the recognized token is a keyword, add it to the sequence just as a keyword. 
                        if (CompilerEnvironment.isKeyword(lexeme)) {
                            token[0] = CompilerEnvironment.getTokenId(lexeme);
                            token[1] = "";
                            sequenceOfTokens.add(token);
                        } 
                        // If the recognized token is an identifier, add it to the identifiers' symbol table
                        // and add a token to the sequence referencing to that new record of the symbol table.
                        else {
                            CompilerEnvironment.setIdentifierSymbolTable(lexeme);
                            token[0] = CompilerEnvironment.getTokenId("identifier");
                            token[1] = CompilerEnvironment.getIdentifierSymbolTableIndex();
                            sequenceOfTokens.add(token);
                        }
                    }

                    // If the recognized token is a number, add it to the numbers' symbol table
                    // and add a token to the sequence referencing to that new record of the symbol table.
                    else if (state == CompilerEnvironment.NUMBER_TOKEN) {
                        CompilerEnvironment.setNumberSymbolTable(Integer.parseInt(lexeme));
                        token[0] = CompilerEnvironment.getTokenId("number");
                        token[1] = CompilerEnvironment.getNumberSymbolTableIndex();
                        sequenceOfTokens.add(token);
                    }

                    // If the recognized token is not a identifier, keyword nor number.
                    else {
                        // If the recognized token is not a comment, add it to the sequence of tokens. If it is a comment
                        // there won't be added any token to the sequence.
                        if(state != CompilerEnvironment.COMMENT_TOKEN) {
                            token[0] = CompilerEnvironment.getTokenId(lexeme);
                            token[1] = "";
                            sequenceOfTokens.add(token);
                        }
                    }

                    // Reset the state to 0 and empty the currently recognized lexeme.
                    state = 0;
                    lexeme = "";
                }

                // If the new state is an error state, print the corresponding error message and abort the 
                // execution of the program.
                else if (state >=  CompilerEnvironment.FIRST_STATE_OF_ERROR) {
                    switch (state) {
                        case CompilerEnvironment.INVALID_IDENTIFIER_ERROR:
                            System.out.println("Invalid identifier error");
                            System.exit(1);
                        case CompilerEnvironment.INVALID_NUMBER_ERROR:
                            System.out.println("Invalid number error");
                            System.exit(1);
                        case CompilerEnvironment.INVALID_LOGIC_OPERATOR_ERROR:
                            System.out.println("Invalid logic operator");
                            System.exit(1);
                        case CompilerEnvironment.INVALID_CHARACTER_ERROR:
                            System.out.println("Invalid character");
                            System.exit(1);
                    }
                    System.exit(1);
                }
            }
            
            // Print the sequence of tokens
            for(Object[] tok : sequenceOfTokens) {
                System.out.println(tok[0] + " " + tok[1]);             
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
