/* 
The CompilerEnvironment class stores all constant and non constant variables used by the Compiler class in order
to construct the sequence of tokens and the symbol tables corresponding to the lexical analysis phase. It also 
contains a few methods to obtain or modify the values of the variables within the class. The class contains the 
implementation of some important data structures as the set to store the keywords, dictionary to determine the token
ids, bidimensional array to handle the transition table, and lists to manage the symbol tables.
*/

import java.util.*;

public class CompilerEnvironment {
    
    // Set used to store the allowed keywords of the language.
    private static final Set<String> KEYWORDS;
    static {
        KEYWORDS = new HashSet<String>();
        KEYWORDS.addAll( Arrays.asList(
            new String[] {
                "else", "if", "int", "return", "void", "while", "input", "output"
            }
        ));
    }

    // Dictionary used to consult the token ids given their token name.
    private static final Map<String, Integer> TOKEN_IDS;
    static {
        TOKEN_IDS = new HashMap<String, Integer>();
        TOKEN_IDS.put("identifier", 0);
        TOKEN_IDS.put("number", 1);
        TOKEN_IDS.put("/", 2);
        TOKEN_IDS.put("*", 3);
        TOKEN_IDS.put("+", 4);
        TOKEN_IDS.put("-", 5);
        TOKEN_IDS.put("<", 6);
        TOKEN_IDS.put("<=", 7);
        TOKEN_IDS.put(">", 8);
        TOKEN_IDS.put(">=", 9);
        TOKEN_IDS.put("=", 10);
        TOKEN_IDS.put("==", 11);
        TOKEN_IDS.put("!=", 12);
        TOKEN_IDS.put(";", 13);
        TOKEN_IDS.put(",", 14);
        TOKEN_IDS.put("(", 15);
        TOKEN_IDS.put(")", 16);
        TOKEN_IDS.put("[", 17);
        TOKEN_IDS.put("]", 18);
        TOKEN_IDS.put("{", 19);
        TOKEN_IDS.put("}", 20);
        TOKEN_IDS.put("else", 21);
        TOKEN_IDS.put("if", 22);
        TOKEN_IDS.put("int", 23);
        TOKEN_IDS.put("return", 24);
        TOKEN_IDS.put("void", 25);
        TOKEN_IDS.put("while", 26);
        TOKEN_IDS.put("input", 27);
        TOKEN_IDS.put("output", 28);
        TOKEN_IDS.put("$", 29);
    }
    
    // ASCII character for each permitted symbol of the language.
    private static final int[] UPPERCASE_LETTER = new int[] {65, 90};
    private static final int[] LOWERCASE_LETTER = new int[] {97, 122};
    private static final int[] NUMBER = new int[] {48, 57};
    private static final int PLUS = 43;
    private static final int MINUS = 45;
    private static final int MULTIPLIER = 42;
    private static final int SLASH = 47;
    private static final int LESS_THAN = 60;
    private static final int EQUAL = 61;
    private static final int GREATER_THAN = 62;
    private static final int ADMIRATION = 33;
    private static final int SEMICOLON = 59;
    private static final int COMMA = 44;
    private static final int[] PARENTHESES = new int[] {40, 41};
    private static final int[] SQUARE_BRACKETS = new int[] {91,93};
    private static final int[] CURLY_BRACKETS = new int[] {123, 125};
    private static final int[] WHITESPACES = new int[] { 9, 10, 13, 32 };


    // Number of column in the transition table associated with each different character allowed by the language.
    private static final int SLETTER = 0;
    private static final int SNUMBER = 1;
    private static final int SPLUS = 2;
    private static final int SMINUS = 3;
    private static final int SMULTIPLIER = 4;
    private static final int SSLASH = 5;
    private static final int SLESS_THAN = 6;
    private static final int SEQUAL = 7; 
    private static final int SGREATER_THAN = 8;
    private static final int SADMIRATION = 9;
    private static final int SSEMICOLON = 10;
    private static final int SCOMMA = 11;
    private static final int SOPARENTHESIS = 12;
    private static final int SCPARENTHESIS = 13;
    private static final int SOSQUAREBRACKET = 14;
    private static final int SCSQUAREBRACKET = 15;
    private static final int SOCURLYBRACKET = 16;
    private static final int SCCURLYBRACKET = 17;
    private static final int SWHITESPACE = 18;
    private static final int SANYCHARACTER = 19;


    // Constant variables used to facilitate the identification of the different states of the transition table.
    public static final int INITIAL_STATE = 0;
    public static final int OPEN_COMMENT_STATUS_1 = 4;
    public static final int OPEN_COMMENT_STATUS_2 = 5;
    public static final int BLANK_COLUMN = 18;
    public static final int FIRST_STATE_OF_ACCEPTANCE = 26;
    public static final int ID_TOKEN = 26;
    public static final int NUMBER_TOKEN = 27;
    public static final int COMMENT_TOKEN = 28;
    public static final int FIRST_STATE_OF_ERROR = 48;
    public static final int INVALID_IDENTIFIER_ERROR = 48;
    public static final int INVALID_NUMBER_ERROR = 49;
    public static final int INVALID_LOGIC_OPERATOR_ERROR = 50;
    public static final int INVALID_CHARACTER_ERROR = 51;
    public static final int UNCLOSED_COMMENT_ERROR = 52;
    public static final int EMPTY_FILE_ERROR = 53;

    // Bidimensional array used to represent the transition table. 
    public static final int[][] TRANSITION_TABLE = new int[][] 
    {  /*  l,  n,  +,  -,  *,  /,  <,  =,  >,  !,  ;,  ,,  (,  ),  [,  ],  {,  },  b, any */
        {  1,  2,  8,  9,  7,  3, 10, 14, 12, 16, 18, 19, 20, 21, 22, 23, 24, 25,  0, 51 }, /*State 0*/
        {  1, 48, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 51 }, /*State 1*/
        { 49,  2, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 51 }, /*State 2*/
        { 29, 29, 29, 29,  4, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 51 }, /*State 3*/
        {  4,  4,  4,  4,  5,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4 }, /*State 4*/
        {  4,  4,  4,  4,  4,  6,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4 }, /*State 5*/
        { 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 51 }, /*State 6*/
        { 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 51 }, /*State 7*/
        { 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 51 }, /*State 8*/
        { 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 51 }, /*State 9*/
        { 33, 33, 33, 33, 33, 33, 33, 11, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 51 }, /*State 10*/
        { 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 51 }, /*State 11*/
        { 35, 35, 35, 35, 35, 35, 35, 13, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 51 }, /*State 12*/
        { 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 51 }, /*State 13*/
        { 37, 37, 37, 37, 37, 37, 37, 15, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 37, 51 }, /*State 14*/
        { 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 51 }, /*State 15*/
        { 50, 50, 50, 50, 50, 50, 50, 17, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51 }, /*State 16*/
        { 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 51 }, /*State 17*/
        { 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 51 }, /*State 18*/
        { 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 51 }, /*State 19*/
        { 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 51 }, /*State 20*/
        { 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 43, 51 }, /*State 21*/
        { 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 51 }, /*State 22*/
        { 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 51 }, /*State 23*/
        { 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 51 }, /*State 24*/
        { 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 47, 51 }  /*State 25*/
    };

    // List used to manage the symbol table for identifiers.
    private static ArrayList<String> IDENTIFIER_SYMBOL_TABLE = new ArrayList<>();

    // Method used to obtain the reference for a new record in the symbol table for identifiers. 
    public static int getIdentifierSymbolTableIndex(String id) {
        for(int i=0; i<IDENTIFIER_SYMBOL_TABLE.size(); i++) {
            if(IDENTIFIER_SYMBOL_TABLE.get(i).equals(id)) {
                return i;
            }
        }
        return IDENTIFIER_SYMBOL_TABLE.size() - 1;
    }

    // Method used to insert a new record in the symbol table for identifiers.
    public static void setIdentifierSymbolTable(String id) {
        for(int i=0; i<IDENTIFIER_SYMBOL_TABLE.size(); i++) {
            if(IDENTIFIER_SYMBOL_TABLE.get(i).equals(id)) {
                return;
            }
        }
        IDENTIFIER_SYMBOL_TABLE.add(id);
    }

    // List used to manage the symbol table for numbers.
    private static ArrayList<Integer> NUMBER_SYMBOL_TABLE = new ArrayList<>();
    
    // Method used to obtain the reference for a new record in the symbol table for numbers.
    public static int getNumberSymbolTableIndex(int number) {
        for(int i=0; i<NUMBER_SYMBOL_TABLE.size(); i++) {
            if(NUMBER_SYMBOL_TABLE.get(i).equals(number)) {
                return i;
            }
        }
        return NUMBER_SYMBOL_TABLE.size() -1;
    }

    // Method used to insert a new record in the symbol table for numbers.
    public static void setNumberSymbolTable(int number) {
        for(int i=0; i<NUMBER_SYMBOL_TABLE.size(); i++) {
            if(NUMBER_SYMBOL_TABLE.get(i).equals(number)){
                return;
            }
        }
        NUMBER_SYMBOL_TABLE.add(number);
    }

    // Method used in order to obtain the whole symbol table for identifiers.
    public static ArrayList<String> getIdentifierSymbolTable() {
        return IDENTIFIER_SYMBOL_TABLE;
    }

    // Method used in order to obtain the whole symbol table for numbers.
    public static ArrayList<Integer> getNumberSymbolTable() {
        return NUMBER_SYMBOL_TABLE;
    }

    // Method used to get the column's index value in the transition table of a determined character given its ASCII code.
    public static int getColumnNumber (int ascii_character) {
        int column = 0;

        // The character is a letter.
        if ((ascii_character >= UPPERCASE_LETTER[0] && ascii_character <= UPPERCASE_LETTER[1]) || 
                ascii_character >= LOWERCASE_LETTER[0] && ascii_character <= LOWERCASE_LETTER[1]) {
            column = SLETTER;
        }
        // The character is a number.
        else if(ascii_character >= NUMBER[0] && ascii_character <= NUMBER[1]) {
            column = SNUMBER;
        }
        // The character is the plus symbol.
        else if(ascii_character == PLUS) {
            column = SPLUS;
        }
        // The character is the minus symbol.
        else if(ascii_character == MINUS) {
            column = SMINUS;
        }
        // The character is the multiplication symbol.
        else if(ascii_character == MULTIPLIER) {
            column = SMULTIPLIER;
        }
        // The character is the slash symbol.
        else if(ascii_character == SLASH) {
            column = SSLASH;
        }
        // The character is the less than symbol.
        else if(ascii_character == LESS_THAN) {
            column = SLESS_THAN;
        }
        // The character is the equal symbol.
        else if(ascii_character == EQUAL) {
            column = SEQUAL;
        }
        // The character is the greater than symbol.
        else if(ascii_character == GREATER_THAN) {
            column = SGREATER_THAN;
        }
        // The character is the admiration symbol.
        else if(ascii_character == ADMIRATION) {
            column = SADMIRATION;
        }
        // The character is the semicolon symbol.
        else if(ascii_character == SEMICOLON) {
            column = SSEMICOLON;
        }
        // The character is the comma symbol.
        else if(ascii_character == COMMA) {
            column = SCOMMA;
        }
        // The character is the open parenthesis.
        else if(ascii_character == PARENTHESES[0]) {
            column = SOPARENTHESIS;
        }
        // The character is the close parenthesis.
        else if(ascii_character == PARENTHESES[1]) {
            column = SCPARENTHESIS;
        }
        // The character is the open square bracket
        else if(ascii_character == SQUARE_BRACKETS[0]) {
            column = SOSQUAREBRACKET;
        }
        // The character is the close square bracket.
        else if(ascii_character == SQUARE_BRACKETS[1]) {
            column = SCSQUAREBRACKET;
        }
        // The character is the open curly bracket.
        else if(ascii_character == CURLY_BRACKETS[0]) {
            column = SOCURLYBRACKET;
        }
        // The character is the close curly bracket.
        else if(ascii_character == CURLY_BRACKETS[1]) {
            column = SCCURLYBRACKET;
        }
        // The character is a whitespace.
        else if(ascii_character == WHITESPACES[0] || ascii_character == WHITESPACES[1] ||
                ascii_character == WHITESPACES[2] || ascii_character == WHITESPACES[3]) {
            column = SWHITESPACE;
        }
        // The character is not listed as a permitted symbol.
        else {
            column = SANYCHARACTER;
        }

        return column;
    }

    // Method used to determine whether a lexeme is a keyword or not. This method retrieves the information from the
    // KEYWORDS set.
    public static boolean isKeyword(String lexeme) {
        return KEYWORDS.contains(lexeme);
    }

    // Method used to obtain the token id given its token name. This method retrieves the information from the 
    // TOKEN_IDS dictionary.
    public static int getTokenId(String lexeme) {
        return TOKEN_IDS.get(lexeme).intValue();
    }

}
