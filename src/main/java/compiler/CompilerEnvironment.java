import java.util.*;

public class CompilerEnvironment {
    
    private static final Set<String> KEYWORDS;

    // Set of keywords
    static {
        KEYWORDS = new HashSet<String>();
        KEYWORDS.addAll( Arrays.asList(
            new String[] {
                "else", "if", "int", "return", "void", "while", "input", "output"
            }
        ));
    }
    
    // ASCII character for each permitted symbol.
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

    // Character mapped to columns of the transition table.
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

    // Variables to handle easily the different states.
    public static final int FIRST_STATE_OF_ACCEPTANCE = 11;
    public static final int BLANK_COLUMN = 18;
    public static final int ID_TOKEN = 11;
    public static final int NUMBER_TOKEN = 12;
    public static final int COMMENT_TOKEN = 13;
    public static final int SLASH_TOKEN = 14;
    public static final int MULTIPLIER_TOKEN = 15;
    public static final int PLUS_TOKEN = 16;
    public static final int MINUS_TOKEN = 17;
    public static final int LESS_THAN_TOKEN = 18;
    public static final int LESS_EQUAL_THAN_TOKEN = 19;
    public static final int GREATER_THAN_TOKEN = 20;
    public static final int GREATER_EQUAL_TOKEN = 21;
    public static final int ASSIGNMENT_TOKEN = 22;
    public static final int LOGIC_EQUAL_TOKEN = 23;
    public static final int NOT_EQUAL_TOKEN = 24; 
    public static final int SEMICOLON_TOKEN = 25;
    public static final int COMMA_TOKEN = 26;
    public static final int OPEN_PARENTHESIS_TOKEN = 27;
    public static final int CLOSE_PARENTHESIS_TOKEN = 28;
    public static final int OPEN_SQUARE_BRACKET_TOKEN = 29;
    public static final int CLOSE_SQUARE_BRACKET_TOKEN = 30;
    public static final int OPEN_CURLY_BRACKET_TOKEN = 31;
    public static final int CLOSE_CURLY_BRACKET_TOKEN = 32;
    public static final int FIRST_STATE_OF_ERROR = 33;


    // TODO: Add the possibility to type any character inside a comment.
    public static final int[][] TRANSITION_TABLE = new int[][] 
    {  /*  l,  n,  +,  -,  *,  /,  <,  =,  >,  !,  ;,  ,,  (,  ),  [,  ],  {,  },  b */
        {  1,  2, 16, 17,  6,  3,  7,  9,  8, 10, 25, 26, 27, 28, 29, 30, 31, 32,  0 }, /*State 0*/
        {  1, 33, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11 }, /*State 1*/
        { 34,  2, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 }, /*State 2*/
        { 14, 14, 35, 35,  4, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 14 }, /*State 3*/
        {  4,  4,  4,  4,  5,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4 }, /*State 4*/
        {  4,  4,  4,  4,  4, 13,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4 }, /*State 5*/
        { 15, 15, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 15 }, /*State 6*/
        { 18, 18, 36, 36, 36, 36, 36, 19, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 18 }, /*State 7*/
        { 20, 20, 36, 36, 36, 36, 36, 21, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 20 }, /*State 8*/
        { 22, 22, 36, 36, 36, 36, 36, 23, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 22 }, /*State 9*/
        { 36, 36, 36, 36, 36, 36, 36, 24, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 37 }  /*State 10*/
    };

    private static final ArrayList<String> IDENTIFIER_SYMBOL_TABLE = new ArrayList<>();
    private static final ArrayList<Integer> NUMBER_SYMBOL_TABLE = new ArrayList<>();

    public static void setIdentifierSymbolTable(String id) { 
        IDENTIFIER_SYMBOL_TABLE.add(id);
    }
    public static int getIdentifierSymbolTableIndex() {
        return IDENTIFIER_SYMBOL_TABLE.size() - 1;
    }

    public static void setNumberSymbolTable(int number) {
        NUMBER_SYMBOL_TABLE.add(number);
    }

    public static int getNumberSymbolTableIndex() {
        return NUMBER_SYMBOL_TABLE.size() -1;
    }

    public static ArrayList<String> getIdentifierSymbolTable() {
        return IDENTIFIER_SYMBOL_TABLE;
    }
    public static ArrayList<Integer> getNumberSymbolTable() {
        return NUMBER_SYMBOL_TABLE;
    }

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
            column = -1;
        }

        return column;
    }

    public static boolean isKeyword(String lexeme) {
        return KEYWORDS.contains(lexeme);
    }
}
