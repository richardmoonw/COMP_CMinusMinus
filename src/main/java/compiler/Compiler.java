/* 
The Compiler class is the one that can be directly invoked from the command console in order to begin the execution
of the lexical and syntactic analyzer. It is in charge of receiving the arguments of its invocation, validate their 
number and finish the execution of the program if a error is raised. Then, the class iterate over all the characters 
in the source code in order to determine whether they can be defined as valid tokens or they have to be identified as 
errors, and print the sequence of tokens and symbol tables produced during the lexical analysis. After that, the program
validates the syntactic structure of the elements from the sequence of tokens, according to a symbols stack and a 
previously defined parsing table. Finally, the class is in charge of determining whether a sequence of tokens have a valid 
syntactic structure or not, and if it actually have it, the symbol tables should be printed with some semantic tags added 
during the previous processes. 

The Compiler class uses the FileManager class in order to have access to the source code from a single variable and to 
finish the execution of the program in the case that any error is found while opening or reading the file. 
The Compiler class uses the CompilerEnvironment class to manage and have access to the different variables that define 
the general behaviour of the required data structures for the correct operation of the lexical analyzer.
The Compiler class uses the SyntacticEnvironment class to mananage and have access to the different variables that define
the general behaviour of the required data structures for the correct operation of the syntactic analyzer.
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

            
            // SYNTACTIC ANALYSIS STARTING POINT.
            System.out.println("\n------------------------------------------------\n");

            // Add the '$' token at the end of the sequence of tokens in order to be able to determine whether a program has been fully syntactically analized 
            // or not.
            Object[] end_token = new Object[2];
            end_token[0] = CompilerEnvironment.getTokenId("$");
            end_token[1] = "";
            sequenceOfTokens.add(end_token);

            // Declare and initialize the variables that will be used during the syntactic analysis to record the updates related to
            // semantic tags in the identifiers' and numbers' symbol tables.
            Object[][] IDENTIFIER_SYMBOL_TABLE = new Object[CompilerEnvironment.getIdentifierSymbolTable().size()][3];
            Object[][] NUMBER_SYMBOL_TABLE = new Object[CompilerEnvironment.getNumberSymbolTable().size()][2];
            SyntacticEnvironment.initializeSemantics(CompilerEnvironment.getIdentifierSymbolTable().size(), CompilerEnvironment.getNumberSymbolTable().size());

            // Stack used to store the symbols obtained from the different production rules of the CFG according to the tokens read 
            // from the token sequence. Each element of the stack represents the id of a terminal or non-terminal symbol of the 
            // grammar.
            Stack<Integer> symbolsStack = new Stack<>();

            // Add the $ symbol as the first element of the stack of symbols in order to determine whether the syntactic analysis is finished or not.
            symbolsStack.push(SyntacticEnvironment.DOLAR);

            // Add the starting symbol of the CFG to start the syntactic analysis over the sequence of tokens.
            symbolsStack.push(SyntacticEnvironment.PROGRAM);

            // Define the variables that will be used while performing the syntactic analysis over the sequence of tokens.
            Object[] currentToken = new Object[2];
            int currentTokenCounter = 0;
            int readToken = 0;
            int previousRule = 0;
            String error;
            int newProduction;
            ArrayList<Integer> newSymbols;

            // While the element at the top of the stack is not the $ token (EOF).
            while(symbolsStack.peek() != SyntacticEnvironment.DOLAR) {

                // Read the next token available within the sequence of tokens.
                currentToken = sequenceOfTokens.get(currentTokenCounter);
                readToken = (int) currentToken[0];

                // If the element at the top of the stack is a terminal symbol, and it is equal to the current token.
                if (symbolsStack.peek() < SyntacticEnvironment.TERMINALS_HIGHER_BOUND && (symbolsStack.peek() == readToken)) {

                    // If the current token is an identifier. Determine whether it is a variable, function or variable/function name,
                    // and whether it has a local, global or local/global scope and add that properties to the temporal identifiers' 
                    // symbols table.
                    if ((int) currentToken[0] == SyntacticEnvironment.IDENTIFIER) {
                        SyntacticEnvironment.assignTokenTypeToIdentifiers(currentToken, sequenceOfTokens.get(currentTokenCounter + 1));
                        SyntacticEnvironment.assignTokenScopeToIdentifiers(currentToken, previousRule);
                    } 
                    
                    // If the current token is a number. Add the corresponding number type property to the temporal numbers' symbols table.
                    else if((int) currentToken[0] == SyntacticEnvironment.NUMBER) {
                        SyntacticEnvironment.assignTokenTypeToNumbers(currentToken);
                    }
                    
                    // Remove the symbol at the top of the stack and increment the current token counter by 1.
                    symbolsStack.pop();
                    currentTokenCounter++;
                }

                // If the element at the top of the stack is a terminal, but it is not equal to the current token, print the corresponding
                // error message and finish the execution of the program.
                else if(symbolsStack.peek() < SyntacticEnvironment.TERMINALS_HIGHER_BOUND && (symbolsStack.peek() != readToken)) {
                    System.out.println(String.format("ERROR: Unexpected token found. It was expected a %s, but it was obtained a %s. To correct the error look at your token #%s",
                        SyntacticEnvironment.TOKENS[symbolsStack.peek()], SyntacticEnvironment.TOKENS[readToken], currentTokenCounter));
                    System.exit(1);
                }

                // If the element at the top of the stack is a non-terminal that produces an error according to the value present on the LL(1) parsing table
                // for that non-terminal symbol and the current token. Then, print the corresponding error message and finish the execution of the program.
                else if(SyntacticEnvironment.PARSING_TABLE[symbolsStack.peek()-SyntacticEnvironment.TERMINALS_HIGHER_BOUND][readToken] < SyntacticEnvironment.ERROR_LIMIT) {
                    error = SyntacticEnvironment.getErrorDescription(SyntacticEnvironment.PARSING_TABLE[symbolsStack.peek()-SyntacticEnvironment.TERMINALS_HIGHER_BOUND][readToken], 
                        currentTokenCounter, readToken);
                    System.out.println(error);
                    System.exit(1);
                }

                // If the element at the top of the stack is a non-terminal that does not produce an error according to the value present on the LL(1) parsing
                // table for that non-terminal symbol and the current token. 
                else {

                    // Get the number of the new production rule from the LL(1) parsing table.
                    newProduction = SyntacticEnvironment.PARSING_TABLE[symbolsStack.peek()-SyntacticEnvironment.TERMINALS_HIGHER_BOUND][readToken];

                    // Retrieve the body of the production rule.
                    newSymbols = SyntacticEnvironment.PRODUCTION_RULES[newProduction];

                    // Remove the symbol at the top of the stack, and insert the terminal and non-terminal symbols that compose the body of the new production
                    // rule. If there are no symbols within the body of the production rule (there is an epsilon), then just remove the symbol at the top of the 
                    // stack. 
                    previousRule = symbolsStack.pop();
                    if(newSymbols.size() > 0) {
                        for(int i=newSymbols.size()-1; i>=0; i--) {
                            symbolsStack.push(newSymbols.get(i));
                        }
                    }
                }
            }

            // If the symbol at the top of the stack and the current token are the $ token (EOF).
            if(symbolsStack.peek() == SyntacticEnvironment.DOLAR && (currentToken[0].equals(SyntacticEnvironment.DOLAR))) {

                // Retrieve the symbols tables generated during the lexical analysis.
                ArrayList<String> identifiers = CompilerEnvironment.getIdentifierSymbolTable();
                ArrayList<Integer> numbers = CompilerEnvironment.getNumberSymbolTable();

                // For each entry of the original identifiers' symbols table.
                for(int i=0; i< identifiers.size(); i++) {

                    // Merge the corresponding identifier, identifier type and identifier scope in a record of the new and updated identifiers'
                    // symbols table.
                    IDENTIFIER_SYMBOL_TABLE[i][0] = identifiers.get(i);
                    IDENTIFIER_SYMBOL_TABLE[i][1] = SyntacticEnvironment.getIdentifiersSemanticSymbolTable(i, 0);
                    IDENTIFIER_SYMBOL_TABLE[i][2] = SyntacticEnvironment.getIdentifiersSemanticSymbolTable(i, 1);

                    // If the scope of the identifier has not been set, then trigger a semantic error due to use of undefined variable or function.
                    if((int) IDENTIFIER_SYMBOL_TABLE[i][2] == SyntacticEnvironment.UNDEFINED) {
                        System.out.println(String.format("SEMANTIC ERROR: The function/variable %s is used but never defined", IDENTIFIER_SYMBOL_TABLE[i][0]));
                        System.exit(1);
                    }
                }

                // For each entry of the original numbers' table, merge the corresponding number and number type in a record of the new and updated
                // numbers' symbols table.
                for(int i=0; i< numbers.size(); i++) {
                    NUMBER_SYMBOL_TABLE[i][0] = numbers.get(i);
                    NUMBER_SYMBOL_TABLE[i][1] = SyntacticEnvironment.getNumbersSemanticSymbolTable(i);
                }

                // Print the new symbols table that include the identified semantic tags.
                SyntacticEnvironment.printSymbolsTable(IDENTIFIER_SYMBOL_TABLE, 0);
                System.out.println();
                SyntacticEnvironment.printSymbolsTable(NUMBER_SYMBOL_TABLE, 1);
                System.out.println("Syntactic Analysis passed successfully");
            } 

            // If the symbol at the top of the stack or the current token are not the $ token (EOF), then print the corresponding error message and exit the program.
            else {
                System.out.println("ERROR: Syntactic analysis could not be finished successfully");
                System.exit(1);
            }
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


