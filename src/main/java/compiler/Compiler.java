/* 
The Compiler class is the one that can be directly invoked from the command console in order to begin the execution
of the lexical analyzer. It is in charge of receiving the arguments of its invocation, validate their number, finish
the execution of the program if a error is raised, iterate over all the characters in the source code in order to 
determine whether they can be defined as valid tokens or they have to be identified as errors, and print the final 
results. 

The Compiler class uses the FileManager class in order to have access to the source code from a single variable and to 
finish the execution of the program in the case that any error is found while opening or reading the file. 
The Compiler class uses the CompilerEnvironment class to manage and have access to the different variables that define 
the general behaviour of the required data structures for the correct operation of the lexical analyzer.  
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Compiler {
    public static void main(String[] argv) {

        // Verify the number of arguments passed to the program. If the number of arguments is not 1, print an error
        // message and finish the execution of the program.
        if(argv.length != 1){
            System.out.println("BAD NUMBER OF ARGUMENTS: YOU MUST SPECIFY ONLY THE FILENAME THAT CONTAINS THE SOURCE CODE");
            System.exit(1);
        }

       try {
            // Read the content of the specified file and store it in a String variable. Trim() function is used to delete all
            // the unnecesary blanks at the beginning or at the end of the source code.
            String sourceCode = FileManager.readFile(argv[0]).trim() + " "; 

            // Define the variables that will be used while iterating over the source code. 
            int state = CompilerEnvironment.INITIAL_STATE;
            int readSourceCode = 0; 
            int character = 0;
            String lexeme = "";
            int column = 0;

            // Vector used to store the token sequence produced by the lexical analyzer. Each element stores an array of objects 
            // because the structure of a token is not always the same. Sometimes it only have the token_id and the second element 
            // of the tuple is empty (special symbols and keywords), but in other cases, a token is composed by its token_id and a
            // reference to a symbol table (identifiers and numbers).
            Vector<Object[]> sequenceOfTokens = new Vector<Object[]>();

            
            // Read characters from the sourceCode until the end of the string is found.
            while(readSourceCode < sourceCode.toCharArray().length) {

                // While the current state is a non terminal state.
                while(state < CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE && readSourceCode <= sourceCode.toCharArray().length) {

                    // If there are still characters to be read in the sourceCode, read the next character.
                    if(readSourceCode < sourceCode.toCharArray().length) {
                        character = sourceCode.toCharArray()[readSourceCode];
                    }

                    // Get the column's index value in the transition table for the given ASCII code of the character read.
                    column = CompilerEnvironment.getColumnNumber(character);

                    // Calculate the new state as a function of the current state and the new character read that is represented by the
                    // column's index value already calculated.
                    state =  CompilerEnvironment.TRANSITION_TABLE[state][column];

                    // If the current character is the EOF and the new state is non terminal.
                    if(readSourceCode == sourceCode.toCharArray().length &&
                            state < CompilerEnvironment.FIRST_STATE_OF_ACCEPTANCE) {
                        
                        // If the state is a subset of the states that represent an unclosed comment.
                        if(state == CompilerEnvironment.OPEN_COMMENT_STATUS_1 || state == CompilerEnvironment.OPEN_COMMENT_STATUS_2) {
                            state = CompilerEnvironment.UNCLOSED_COMMENT_ERROR; 
                        } 
                        // If the state is the initial state.
                        else if(state == CompilerEnvironment.INITIAL_STATE) {
                            state = CompilerEnvironment.EMPTY_FILE_ERROR;
                        }
                    }
                    
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

                // If the current state is an state of acceptance.
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
                        // If the recognized token is an identifier, add the lexeme to the identifiers' symbol table
                        // and add a token to the sequence referencing to that new record of the symbol table.
                        else {
                            CompilerEnvironment.setIdentifierSymbolTable(lexeme);
                            token[0] = CompilerEnvironment.getTokenId("identifier");
                            token[1] = CompilerEnvironment.getIdentifierSymbolTableIndex(lexeme);
                            sequenceOfTokens.add(token);
                        }
                    }

                    // If the recognized token is a number, add the lexeme to the numbers' symbol table
                    // and add a token to the sequence referencing to that new record of the symbol table.
                    else if (state == CompilerEnvironment.NUMBER_TOKEN) {
                        CompilerEnvironment.setNumberSymbolTable(Integer.parseInt(lexeme));
                        token[0] = CompilerEnvironment.getTokenId("number");
                        token[1] = CompilerEnvironment.getNumberSymbolTableIndex(Integer.parseInt(lexeme));
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

                    // Reset the state to the initial value and empty the currently recognized lexeme.
                    state = CompilerEnvironment.INITIAL_STATE;
                    lexeme = "";
                }

                // If the current state is an error state, print the corresponding error message and abort the 
                // execution of the program.
                else if (state >=  CompilerEnvironment.FIRST_STATE_OF_ERROR) {
                    switch (state) {
                        // Invalid identifier error.
                        case CompilerEnvironment.INVALID_IDENTIFIER_ERROR:
                            System.out.println("EXECUTION FINISHED DUE TO AN INVALID IDENTIFIER ERROR");
                            System.exit(1);

                        // Invalid number error.
                        case CompilerEnvironment.INVALID_NUMBER_ERROR:
                            System.out.println("EXECUTION FINISHED DUE TO AN INVALID NUMBER ERROR");
                            System.exit(1);

                        // Invalid logic operator error.
                        case CompilerEnvironment.INVALID_LOGIC_OPERATOR_ERROR:
                            System.out.println("EXECUTION FINISHED DUE TO AN INVALID LOGIC OPERATOR ERROR");
                            System.exit(1);

                        // Invalid character error.
                        case CompilerEnvironment.INVALID_CHARACTER_ERROR:
                            System.out.println("EXECUTION FINISHED DUE TO AN INVALID CHARACTER ERROR");
                            System.exit(1);

                        // Unclosed comment error.
                        case CompilerEnvironment.UNCLOSED_COMMENT_ERROR:
                            System.out.println("EXECUTION FINISHED DUE TO AN UNCLOSED COMMENT ERROR");
                            System.exit(1);

                        // Empty file error.
                        case CompilerEnvironment.EMPTY_FILE_ERROR:
                            System.out.println("EXECUTION FINISHED DUE TO AN EMPTY FILE ERROR");
                            System.exit(1);
                        
                        // Invalid state error.
                        default:
                            System.out.println("EXECUTION FINISHED DUE TO AN INVALID STATE ERROR");
                            System.exit(1);
                    }
                    System.exit(1);
                }
            }
            
            // Print the sequence of tokens
            for(Object[] tok : sequenceOfTokens) {
                System.out.println(tok[0] + " " + tok[1]);             
            }

            // Print the identifiers' and numbers' symbol table
            System.out.println(CompilerEnvironment.getIdentifierSymbolTable());
            System.out.println(CompilerEnvironment.getNumberSymbolTable());
        }
        // If the invocation of the FileManager's readFile() method throws an exception indicating that 
        // it was not possible to open the file, print the corresponding error and finish the execution 
        // of the program.
        catch (FileNotFoundException fileNotFoundException) {
            System.out.println("EXECUTION FINISHED CAUSE THE FILENAME YOU SPECIFIED DOES NOT EXIST");
            System.exit(1);
        } 
        
        // If the invocation of the FileManager's readFile() method throws an exception indicating that 
        // it occurred an error while reading the content of the file, print the corresponding error and 
        // finish the execution of the program.
        catch (IOException ioException) {
            System.out.println("EXECUTION FINISHED CAUSE THERE WAS A PROBLEM READING THE FILE");
            System.exit(1);
        }
    }

}


