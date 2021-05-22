/* 
The SyntacticEnvironment class stores all constant and non constant variables used by the Compiler class in order
to evaluate the sequence of tokens to determine whether a program has a correct syntactical structure or not. It also 
contains a few methods to obtain or modify the values of the variables within the class. The class contains the 
implementation of some important data structures as the TBD.
*/

import java.util.*;

public class SyntacticEnvironment {

  // Arrays used to store the semantic tags for the different identifiers and numbers found in the source code.
  private static int[][] IDENTIFIERS_SEMANTIC_SYMBOL_TABLE;
  private static int[] NUMBERS_SEMANTIC_SYMBOL_TABLE;

  // Constant values definition for the different semantic tags.
  public static final int IDENTIFIERS_SYMBOL_TABLE_NUMBER = 0;
  public static final int NUMBERS_SYMBOL_TABLE_NUMBER =1;

  private static final int UNDEFINED = 0;
  private static final int TOKEN_TYPE = 0;
  private static final int IDENTIFIER_SCOPE = 1;

  private static final int INTEGER_CONSTANT = 1;

  private static final int VARIABLE_TYPE = 1;
  private static final int FUNCTION_TYPE = 2;
  private static final int VARIABLE_AND_FUNCTION_TYPE = 3;

  private static final int LOCAL_SCOPE = 1;
  private static final int GLOBAL_SCOPE = 2;
  private static final int LOCAL_AND_GLOBAL_SCOPE = 3;  
  
  // # Row of the LL(1) parsing table for each given non terminal symbol of the grammar
  private static final int NON_TERMINAL_SYMBOLS_OFFSET = 100;
  public static int PROGRAM = NON_TERMINAL_SYMBOLS_OFFSET + 0;
  private static int DECLARATION_LIST_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 1;
  private static int DECLARATION = NON_TERMINAL_SYMBOLS_OFFSET + 2;
  private static int DECLARATION_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 3;
  private static int DECLARATION_BIPRIME = NON_TERMINAL_SYMBOLS_OFFSET + 4;
  private static int DECLARATION_TRIPRIME = NON_TERMINAL_SYMBOLS_OFFSET + 5;
  private static int VAR_DECLARATION = NON_TERMINAL_SYMBOLS_OFFSET + 6;
  private static int VAR_DECLARATION_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 7;
  private static int PARAMS = NON_TERMINAL_SYMBOLS_OFFSET + 8;
  private static int PARAM_LIST_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 9;
  private static int PARAM = NON_TERMINAL_SYMBOLS_OFFSET + 10;
  private static int PARAM_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 11;
  private static int COMPOUND_STMT = NON_TERMINAL_SYMBOLS_OFFSET + 12;
  private static int COMPOUND_STMT_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 13;
  private static int COMPOUND_STMT_BIPRIME = NON_TERMINAL_SYMBOLS_OFFSET + 14;
  private static int COMPOUND_STMT_TRIPRIME = NON_TERMINAL_SYMBOLS_OFFSET + 15;
  private static int COMPOUND_STMT_POLYYPRIME = NON_TERMINAL_SYMBOLS_OFFSET + 16;
  private static int COMPOUND_STMT_RET = NON_TERMINAL_SYMBOLS_OFFSET + 17;
  private static int LOCAL_DECLARATIONS = NON_TERMINAL_SYMBOLS_OFFSET + 18;
  private static int LOCAL_DECLARATIONS_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 19;
  private static int STATEMENT_LIST = NON_TERMINAL_SYMBOLS_OFFSET + 20;
  private static int STATEMENT_LIST_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 21;
  private static int STATEMENT = NON_TERMINAL_SYMBOLS_OFFSET + 22;
  private static int STATEMENT_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 23;
  private static int STATEMENT_BIPRIME = NON_TERMINAL_SYMBOLS_OFFSET + 24;
  private static int RETURN_STMT = NON_TERMINAL_SYMBOLS_OFFSET + 25;
  private static int RETURN_STMT_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 26;
  private static int VAR = NON_TERMINAL_SYMBOLS_OFFSET + 27;
  private static int VAR_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 28;
  private static int EXPRESSION = NON_TERMINAL_SYMBOLS_OFFSET + 29;
  private static int EXPRESSION_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 30;
  private static int RELOP = NON_TERMINAL_SYMBOLS_OFFSET + 31;
  private static int ARITHMETIC_EXPRESSION = NON_TERMINAL_SYMBOLS_OFFSET + 32;
  private static int ARITHMETIC_EXPRESSION_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 33;
  private static int ADDOP = NON_TERMINAL_SYMBOLS_OFFSET + 34;
  private static int TERM = NON_TERMINAL_SYMBOLS_OFFSET + 35;
  private static int TERM_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 36;
  private static int MULOP = NON_TERMINAL_SYMBOLS_OFFSET + 37;
  private static int FACTOR = NON_TERMINAL_SYMBOLS_OFFSET + 38;
  private static int FACTOR_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 39;
  private static int ARGS = NON_TERMINAL_SYMBOLS_OFFSET + 40;
  private static int ARGS_LIST_PRIME = NON_TERMINAL_SYMBOLS_OFFSET + 41;

  // # Column of the LL(1) parsing table for each given terminal symbol of the grammar.
  public static int IDENTIFIER = 0;
  public static int NUMBER = 1;
  private static int DIVISION = 2;
  private static int MULTIPLICATION = 3;
  private static int ADDITION = 4;
  private static int SUBSTRACTION = 5;
  private static int LESS_THAN = 6;
  private static int LESS_EQUAL_THAN = 7;
  private static int GREATER_THAN = 8;
  private static int GREATER_EQUAL_THAN = 9;
  private static int ASSIGNMENT = 10;
  private static int EQUAL = 11;
  private static int NOT_EQUAL = 12;
  private static int SEMICOLON = 13;
  private static int COMMA = 14;
  private static int OPEN_PARENTHESIS = 15;
  private static int CLOSE_PARENTHESIS = 16;
  private static int OPEN_SBRACKET = 17;
  private static int CLOSE_SBRACKET = 18;
  private static int OPEN_CBRACKET = 19;
  private static int CLOSE_CBRACKET = 20;
  private static int ELSE = 21;
  private static int IF = 22;
  private static int INT = 23;
  private static int RETURN = 24;
  private static int VOID = 25;
  private static int WHILE = 26;
  private static int INPUT = 27;
  private static int OUTPUT = 28;
  public static int DOLAR = 29;
  
  // Different error numeric constants definition. The following variables only determine a numeric error value for the different possible
  // errors during the syntactic analysis, the explanation for each error is defined later on the code.
  public static final int ERROR_LIMIT = 0;
  public static final int ER1 = -1;     /* PROGRAM ERROR */
  public static final int ER2 = -2;     /* DECLARATION_LIST_PRIME ERROR */      
  public static final int ER3 = -3;     /* DECLARATION ERROR */ 
  public static final int ER4 = -4;     /* DECLARATION_PRIME ERROR */ 
  public static final int ER5 = -5;     /* DECLARATION_BIPRIME ERROR */ 
  public static final int ER6 = -6;     /* DECLARATION_TRIPRIME ERROR */ 
  public static final int ER7 = -7;     /* VAR_DECLARATION ERROR */ 
  public static final int ER8 = -8;     /* VAR_DECLARATION_PRIME ERROR */ 
  public static final int ER9 = -9;     /* PARAMS ERROR */ 
  public static final int ER10 = -10;   /* PARAM_LIST_PRIME ERROR */ 
  public static final int ER11 = -11;   /* PARAM ERROR */ 
  public static final int ER12 = -12;   /* PARAM_PRIME ERROR */ 
  public static final int ER13 = -13;   /* COMPOUND_STMT ERROR */ 
  public static final int ER14 = -14;   /* COMPOUND_STMT_PRIME ERROR */ 
  public static final int ER15 = -15;   /* COMPOUND_STMT_BIPRIME ERROR */ 
  public static final int ER16 = -16;   /* COMPOUND_STMT_TRIPRIME ERROR */ 
  public static final int ER17 = -17;   /* COMPOUND_STMT_POLYPRIME ERROR */ 
  public static final int ER18 = -18;   /* COMPOUND_STMT_RET ERROR */ 
  public static final int ER19 = -19;   /* LOCAL_DECLARATIONS ERROR */
  public static final int ER20 = -20;   /* LOCAL_DECLARATIONS_PRIME ERROR */
  public static final int ER21 = -21;   /* STATEMENT_LIST ERROR */
  public static final int ER22 = -22;   /* STATEMENT_LIST_PRIME ERROR */
  public static final int ER23 = -23;   /* STATEMENT ERROR */
  public static final int ER24 = -24;   /* STATEMENT_PRIME ERROR */
  public static final int ER25 = -25;   /* STATEMENT_BIPRIME ERROR */
  public static final int ER26 = -26;   /* RETURN_STMT ERROR */
  public static final int ER27 = -27;   /* RETURN_STMT_PRIME ERROR */
  public static final int ER28 = -28;   /* VAR ERROR */
  public static final int ER29 = -29;   /* VAR_PRIME ERROR */
  public static final int ER30 = -30;   /* EXPRESSION ERROR */
  public static final int ER31 = -31;   /* EXPRESSION_PRIME ERROR */
  public static final int ER32 = -32;   /* RELOP ERROR */
  public static final int ER33 = -33;   /* ARITHMETIC_EXPRESSION ERROR */
  public static final int ER34 = -34;   /* ARITHMETIC_EXPRESSION_PRIME ERROR */
  public static final int ER35 = -35;   /* ADDOP ERROR */
  public static final int ER36 = -36;   /* TERM ERROR */
  public static final int ER37 = -37;   /* TERM_PRIME ERROR */
  public static final int ER38 = -38;   /* MULOP ERROR */
  public static final int ER39 = -39;   /* FACTOR ERROR */
  public static final int ER40 = -40;   /* FACTOR_PRIME ERROR */
  public static final int ER41 = -41;   /* ARGS ERROR */
  public static final int ER42 = -42;   /* ARGS_LIST_PRIME ERROR */

  // Syntactical error messages definition.
  private static String ERM1 = "ERROR: Wrong variable or function definition. int or void are the possible options, but it was obtained %s";                      /* PROGRAM ERROR */
  private static String ERM2 = "ERROR: Wrong variable or function definition. int, void or EOF are the possible options, but it was obtained %s";                 /* DECLARATION_LIST_PRIME ERROR */      
  private static String ERM3 = "ERROR: Wrong variable or function definition. int or void are the possible options, but it was obtained %s";                      /* DECLARATION ERROR */ 
  private static String ERM4 = "ERROR: Wrong int variable or function definition. ;, ( or [ are the possible options,  but it was obtained %s";                   /* DECLARATION_PRIME ERROR */ 
  private static String ERM5 = "ERROR: Wrong void function definition. { is the possible option, but it was obtained %s";                                         /* DECLARATION_BIPRIME ERROR */ 
  private static String ERM6 = "ERROR: Wrong void function definition. } or return are the possible options, but it was obtained %s";                             /* DECLARATION_TRIPRIME ERROR */ 
  private static String ERM7 = "ERROR: Wrong variable definition. int is the possible option, but it was obtained %s";                                            /* VAR_DECLARATION ERROR */ 
  private static String ERM8 = "ERROR: Wrong variable definition. ; or [ are the possible options, but it was obtained %s";                                       /* VAR_DECLARATION_PRIME ERROR */ 
  private static String ERM9 = "ERROR: Wrong function's params definition. int or void are the possible options, but it was obtained %s";                         /* PARAMS ERROR */ 
  private static String ERM10 = "ERROR: Wrong function's param list definition. , or ) are the possible options, but it was obtained %s";                         /* PARAM_LIST_PRIME ERROR */ 
  private static String ERM11 = "ERROR: Wrong function's param definition. int is the possible option, but it was obtained %s";                                   /* PARAM ERROR */ 
  private static String ERM12 = "ERROR: Wrong function's param definition. ,, ) or [ are the possible options, but it was obtained %s";                           /* PARAM_PRIME ERROR */ 
  private static String ERM13 = "ERROR: Wrong statement declaration. ID, {, if, return, while, input or output are the possible options, but it was obtained %s"; /* COMPOUND_STMT ERROR */ 
  private static String ERM14 = "ERROR: Wrong enclosed declaration. } or return are the possible options, but it was obtained %s";                                /* COMPOUND_STMT_PRIME ERROR */ 
  private static String ERM15 = "ERROR: Wrong assignment or function call definition. =, (, [ are the possible options, but it was obtained %s";                  /* COMPOUND_STMT_BIPRIME ERROR */
                                                                                                                                                                  /* STATEMENT_PRIME ERROR */ 
  private static String ERM16 = "ERROR: Wrong if statement definition. ID, else, if, return, while, input or output are the possible options, but it was obtained %s";    /* COMPOUND_STMT_TRIPRIME ERROR */ 
  private static String ERM17 = "ERROR: Wrong return definition. ID, NUM, ; or ( are the possible options, but it was obtained %s";                               /* COMPOUND_STMT_POLYPRIME ERROR */ 
  private static String ERM18 = "ERROR: Wrong int function definition. { is the possible option, but it was obtained %s";                                         /* COMPOUND_STMT_RET ERROR */ 
  private static String ERM19 = "ERROR: Wrong local declarations. ID, }, if, int, return, while, input or output are the possible options, but it was obtained %s";       /* LOCAL_DECLARATIONS ERROR */
                                                                                                                                                                          /* LOCAL_DECLARATIONS_PRIME ERROR */
  private static String ERM20 = "ERROR: Wrong statement list definition. ID, }, if, int, return, while, input or output are the possible options, but it was obtained %s";   /* STATEMENT_LIST ERROR */
                                                                                                                                                                  /* STATEMENT_LIST_PRIME ERROR */
  private static String ERM21 = "ERROR: Wrong single statement declaration. ID, if, while, input or output are the possible options, but it was obtained %s";     /* STATEMENT ERROR */
  private static String ERM22 = "ERROR: Wrong if statement definition. ID, }, else, if, return, while, input, output are the possible options, but it was obtained %s";      /* STATEMENT_BIPRIME ERROR */
  private static String ERM23 = "ERROR: Wrong return definition. return is the possible option, but it was obtained %s";                                          /* RETURN_STMT ERROR */
  private static String ERM24 = "ERROR: Wrong return definition. ID, NUM, ; or ( are the possible options, but it was obtained %s";                               /* RETURN_STMT_PRIME ERROR */
  private static String ERM25 = "ERROR: Wrong input of variable definition. ID is the possible option, but it was obtained %s";                                   /* VAR ERROR */
  private static String ERM26 = "ERROR: Wrong input of variable definition. ; or [ are the possible options, but it was obtained %s";                             /* VAR_PRIME ERROR */
  private static String ERM27 = "ERROR: Wrong expression definition. ID, NUM or ( are the possible options, but it was obtained %s";                              /* EXPRESSION ERROR */
  private static String ERM28 = "ERROR: Wrong expression definition. <, <=, >, >=, ==, !=, ; or ) are the possible options, but it was obtained %s";              /* EXPRESSION_PRIME ERROR */
  private static String ERM29 = "ERROR: Wrong relational operator definition. <, <=, >, >=, ==, != are the possible options, but it was obtained %s";             /* RELOP ERROR */
  private static String ERM30 = "ERROR: Wrong operand definition. ID, NUM or ( are the possible options, but it was obtained %s";                                 /* ARITHMETIC_EXPRESSION ERROR */
                                                                                                                                                                  /* TERM ERROR */
                                                                                                                                                                  /* FACTOR ERROR */
  private static String ERM31 = "ERROR: Wrong operation definition. +, -, <, <=, >, >=, ==, !=, ;, ,, ) or ] are the possible options, but it was obtained %s";   /* ARITHMETIC_EXPRESSION_PRIME ERROR */
  private static String ERM32 = "ERROR: Wrong arithmetic operator definition. + or - are the possible options, but it was obtained %s";                           /* ADDOP ERROR */
  private static String ERM33 = "ERROR: Wrong operation definition. /, *, +, -, <, <=, >, >=, ==, !=, ;, ,, ) or ] are the possible options, but it was obtained %s";        /* TERM_PRIME ERROR */
  private static String ERM34 = "ERROR: Wrong arithmetic operator definition. / or * are the possible options, but it was obtained %s";                           /* MULOP ERROR */ 
  private static String ERM35 = "ERROR: Wrong operand definition. /, *, +, -, <, <=, >, >=, ==, !=, ;, ,, (, ) [ or ] are the possible options, but it was obtained %s";     /* FACTOR_PRIME ERROR */
  private static String ERM36 = "ERROR: Wrong function's arguments definition. ID, NUM, (, ) are the possible options, but it was obtained %s";                   /* ARGS ERROR */
  private static String ERM37 = "ERROR: Wrong function's argument list definition. , or ) are the possible options, but it was obtained %s";                      /* ARGS_LIST_PRIME ERROR */


  // Production rules for the final Context Free Grammar.
  private static final int NUMBER_OF_RULES = 91;
  public static ArrayList<Integer>[] PRODUCTION_RULES = new ArrayList[NUMBER_OF_RULES];

  static {
    for(int i=0; i<NUMBER_OF_RULES; i++) {
      PRODUCTION_RULES[i] = new ArrayList<Integer>();
    }

    // 0 - PROGRAM
    PRODUCTION_RULES[0].add(DECLARATION);
    PRODUCTION_RULES[0].add(DECLARATION_LIST_PRIME);

    // 1 - DECLARATION_LIST_PRIME
    PRODUCTION_RULES[1].add(DECLARATION);
    PRODUCTION_RULES[1].add(DECLARATION_LIST_PRIME);

    // 2 - DECLARATION *PRODUCES EPSILON*

    // 3 - DECLARATION 
    PRODUCTION_RULES[3].add(INT);
    PRODUCTION_RULES[3].add(IDENTIFIER);
    PRODUCTION_RULES[3].add(DECLARATION_PRIME);

    // 4 - DECLARATION
    PRODUCTION_RULES[4].add(VOID);
    PRODUCTION_RULES[4].add(IDENTIFIER);
    PRODUCTION_RULES[4].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[4].add(PARAMS);
    PRODUCTION_RULES[4].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[4].add(DECLARATION_BIPRIME);

    // 5 - DECLARATION_PRIME
    PRODUCTION_RULES[5].add(SEMICOLON);

    // 6 - DECLARATION_PRIME
    PRODUCTION_RULES[6].add(OPEN_SBRACKET);
    PRODUCTION_RULES[6].add(NUMBER);
    PRODUCTION_RULES[6].add(CLOSE_SBRACKET);
    PRODUCTION_RULES[6].add(SEMICOLON);

    // 7 - DECLARARION_PRIME 
    PRODUCTION_RULES[7].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[7].add(PARAMS);
    PRODUCTION_RULES[7].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[7].add(COMPOUND_STMT_RET);

    // 8 - DECLARATION_BIPRIME
    PRODUCTION_RULES[8].add(OPEN_CBRACKET);
    PRODUCTION_RULES[8].add(LOCAL_DECLARATIONS);
    PRODUCTION_RULES[8].add(STATEMENT_LIST);
    PRODUCTION_RULES[8].add(DECLARATION_TRIPRIME);

    // 9 - DECLARATION_TRIPRIME
    PRODUCTION_RULES[9].add(CLOSE_CBRACKET);

    // 10 - DECLARATION_TRIPRIME
    PRODUCTION_RULES[10].add(RETURN_STMT);
    PRODUCTION_RULES[10].add(CLOSE_CBRACKET);

    // 11 - VAR_DECLARATION
    PRODUCTION_RULES[11].add(INT);
    PRODUCTION_RULES[11].add(IDENTIFIER);
    PRODUCTION_RULES[11].add(VAR_DECLARATION_PRIME);

    // 12 - VAR_DECLARATION_PRIME
    PRODUCTION_RULES[12].add(SEMICOLON);

    // 13 - VAR_DECLARATION PRIME
    PRODUCTION_RULES[13].add(OPEN_SBRACKET);
    PRODUCTION_RULES[13].add(NUMBER);
    PRODUCTION_RULES[13].add(CLOSE_SBRACKET);
    PRODUCTION_RULES[13].add(SEMICOLON);

    // 14 - PARAMS
    PRODUCTION_RULES[14].add(PARAM);
    PRODUCTION_RULES[14].add(PARAM_LIST_PRIME);

    // 15 - PARAMS
    PRODUCTION_RULES[15].add(VOID);

    // 16 - PARAM_LIST_PRIME
    PRODUCTION_RULES[16].add(COMMA);
    PRODUCTION_RULES[16].add(PARAM);
    PRODUCTION_RULES[16].add(PARAM_LIST_PRIME);

    // 17 - PARAM_LIST_PRIME *PRODUCES EPSILON*

    // 18 - PARAM
    PRODUCTION_RULES[18].add(INT);
    PRODUCTION_RULES[18].add(IDENTIFIER);
    PRODUCTION_RULES[18].add(PARAM_PRIME);

    // 19 - PARAM_PRIME *PRODUCES EPSILON*

    // 20 - PARAM_PRIME
    PRODUCTION_RULES[20].add(OPEN_SBRACKET);
    PRODUCTION_RULES[20].add(CLOSE_SBRACKET);

    // 21 - COMPOUND_STMT
    PRODUCTION_RULES[21].add(OPEN_CBRACKET);
    PRODUCTION_RULES[21].add(LOCAL_DECLARATIONS);
    PRODUCTION_RULES[21].add(STATEMENT_LIST);
    PRODUCTION_RULES[21].add(COMPOUND_STMT_PRIME);

    // 22 - COMPOUND_STMT
    PRODUCTION_RULES[22].add(IDENTIFIER);
    PRODUCTION_RULES[22].add(COMPOUND_STMT_BIPRIME);

    // 23 - COMPOUND_STMT
    PRODUCTION_RULES[23].add(IF);
    PRODUCTION_RULES[23].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[23].add(EXPRESSION);
    PRODUCTION_RULES[23].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[23].add(COMPOUND_STMT);
    PRODUCTION_RULES[23].add(COMPOUND_STMT_TRIPRIME);

    // 24 - COMPOUND_STMT
    PRODUCTION_RULES[24].add(WHILE);
    PRODUCTION_RULES[24].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[24].add(EXPRESSION);
    PRODUCTION_RULES[24].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[24].add(COMPOUND_STMT);

    // 25 - COMPOUND_STMT
    PRODUCTION_RULES[25].add(INPUT);
    PRODUCTION_RULES[25].add(VAR);
    PRODUCTION_RULES[25].add(SEMICOLON);

    // 26 - COMPOUND_STMT
    PRODUCTION_RULES[26].add(OUTPUT);
    PRODUCTION_RULES[26].add(EXPRESSION);
    PRODUCTION_RULES[26].add(SEMICOLON);

    // 27 - COMPOUND_STMT
    PRODUCTION_RULES[27].add(RETURN);
    PRODUCTION_RULES[27].add(COMPOUND_STMT_POLYYPRIME);

    // 28 - COMPOUND_STMT_PRIME
    PRODUCTION_RULES[28].add(RETURN_STMT);
    PRODUCTION_RULES[28].add(CLOSE_CBRACKET);

    // 29 - COMPOUND_STMT_PRIME
    PRODUCTION_RULES[29].add(CLOSE_CBRACKET);

    // 30 - COMPOUND_STMT_BIPRIME
    PRODUCTION_RULES[30].add(ASSIGNMENT);
    PRODUCTION_RULES[30].add(EXPRESSION);
    PRODUCTION_RULES[30].add(SEMICOLON);

    // 31 - COMPOUND_STMT_BIPRIME
    PRODUCTION_RULES[31].add(OPEN_SBRACKET);
    PRODUCTION_RULES[31].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[31].add(CLOSE_SBRACKET);
    PRODUCTION_RULES[31].add(ASSIGNMENT);
    PRODUCTION_RULES[31].add(EXPRESSION);
    PRODUCTION_RULES[31].add(SEMICOLON);

    // 32 - COMPOUND_STMT_BIPRIME
    PRODUCTION_RULES[32].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[32].add(ARGS);
    PRODUCTION_RULES[32].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[32].add(SEMICOLON);

    // 33 - COMPOUND_STMT_TRIPRIME *PRODUCES EPSILON*

    // 34 - COMPOUND_STMT_TRIPRIME
    PRODUCTION_RULES[34].add(ELSE);
    PRODUCTION_RULES[34].add(COMPOUND_STMT);

    // 35 - COMPOUND_STMT_POLYPRIME
    PRODUCTION_RULES[35].add(SEMICOLON);

    // 36 - COMPOUND_STMT_POLYPRIME
    PRODUCTION_RULES[36].add(EXPRESSION);
    PRODUCTION_RULES[36].add(SEMICOLON);

    // 37 - COMPOUND_STMT_RET
    PRODUCTION_RULES[37].add(OPEN_CBRACKET);
    PRODUCTION_RULES[37].add(LOCAL_DECLARATIONS);
    PRODUCTION_RULES[37].add(STATEMENT_LIST);
    PRODUCTION_RULES[37].add(RETURN_STMT);
    PRODUCTION_RULES[37].add(CLOSE_CBRACKET);

    // 38 - LOCAL_DECLARATIONS
    PRODUCTION_RULES[38].add(VAR_DECLARATION);
    PRODUCTION_RULES[38].add(LOCAL_DECLARATIONS_PRIME);

    // 39 - LOCAL_DECLARATIONS *PRODUCES EPSILON*

    // 40 - LOCAL_DECLARATIONS_PRIME
    PRODUCTION_RULES[40].add(VAR_DECLARATION);
    PRODUCTION_RULES[40].add(LOCAL_DECLARATIONS_PRIME);

    // 41 - LOCAL_DECLARATIONS_PRIME *PRODUCES EPSILON*

    // 42 - STATEMENT_LIST 
    PRODUCTION_RULES[42].add(STATEMENT);
    PRODUCTION_RULES[42].add(STATEMENT_LIST_PRIME);

    // 43 - STATEMENT_LIST *PRODUCES EPSILON*

    // 44 - STATEMENT_LIST_PRIME
    PRODUCTION_RULES[44].add(STATEMENT); 
    PRODUCTION_RULES[44].add(STATEMENT_LIST_PRIME);

    // 45 - STATEMENT_LIST_PRIME *PRODUCES EPSILON*

    // 46 - STATEMENT
    PRODUCTION_RULES[46].add(IDENTIFIER);
    PRODUCTION_RULES[46].add(STATEMENT_PRIME);

    // 47 - STATEMENT
    PRODUCTION_RULES[47].add(IF);
    PRODUCTION_RULES[47].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[47].add(EXPRESSION);
    PRODUCTION_RULES[47].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[47].add(COMPOUND_STMT);
    PRODUCTION_RULES[47].add(STATEMENT_BIPRIME);

    // 48 - STATEMENT
    PRODUCTION_RULES[48].add(WHILE);
    PRODUCTION_RULES[48].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[48].add(EXPRESSION);
    PRODUCTION_RULES[48].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[48].add(COMPOUND_STMT);

    // 49 - STATEMENT
    PRODUCTION_RULES[49].add(INPUT);
    PRODUCTION_RULES[49].add(VAR);
    PRODUCTION_RULES[49].add(SEMICOLON);

    // 50 - STATEMENT
    PRODUCTION_RULES[50].add(OUTPUT);
    PRODUCTION_RULES[50].add(EXPRESSION);
    PRODUCTION_RULES[50].add(SEMICOLON);

    // 51 - STATEMENT_PRIME
    PRODUCTION_RULES[51].add(ASSIGNMENT);
    PRODUCTION_RULES[51].add(EXPRESSION);
    PRODUCTION_RULES[51].add(SEMICOLON);

    // 52 - STATEMENT_PRIME
    PRODUCTION_RULES[52].add(OPEN_SBRACKET);
    PRODUCTION_RULES[52].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[52].add(CLOSE_SBRACKET);
    PRODUCTION_RULES[52].add(ASSIGNMENT);
    PRODUCTION_RULES[52].add(EXPRESSION);
    PRODUCTION_RULES[52].add(SEMICOLON);

    // 53 - STATEMENT_PRIME
    PRODUCTION_RULES[53].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[53].add(ARGS);
    PRODUCTION_RULES[53].add(CLOSE_PARENTHESIS);
    PRODUCTION_RULES[53].add(SEMICOLON);

    // 54 - STATEMENT_BIPRIME *PRODUCES EPSILON*
    
    // 55 - STATEMENT_BIPRIME
    PRODUCTION_RULES[55].add(ELSE);
    PRODUCTION_RULES[55].add(COMPOUND_STMT);

    // 56 - RETURN_STMT
    PRODUCTION_RULES[56].add(RETURN);
    PRODUCTION_RULES[56].add(RETURN_STMT_PRIME);

    // 57 - RETURN_STMT_PRIME
    PRODUCTION_RULES[57].add(SEMICOLON);

    // 58 - RETURN_STMT_PRIME
    PRODUCTION_RULES[58].add(EXPRESSION);
    PRODUCTION_RULES[58].add(SEMICOLON);

    // 59 - VAR
    PRODUCTION_RULES[59].add(IDENTIFIER);
    PRODUCTION_RULES[59].add(VAR_PRIME);

    // 60 - VAR_PRIME *PRODUCES EPSILON*

    // 61 - VAR_PRIME 
    PRODUCTION_RULES[61].add(OPEN_SBRACKET);
    PRODUCTION_RULES[61].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[61].add(CLOSE_SBRACKET);

    // 62 - EXPRESSION
    PRODUCTION_RULES[62].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[62].add(EXPRESSION_PRIME);

    // 63 - EXPRESSION_PRIME
    PRODUCTION_RULES[63].add(RELOP);
    PRODUCTION_RULES[63].add(ARITHMETIC_EXPRESSION);

    // 64 - EXPRESSION_PRIME *PRODUCES EPSILON*

    // 65 - RELOP
    PRODUCTION_RULES[65].add(LESS_EQUAL_THAN);

    // 66 - RELOP
    PRODUCTION_RULES[66].add(LESS_THAN);

    // 67 - RELOP
    PRODUCTION_RULES[67].add(GREATER_THAN);

    // 68 - RELOP
    PRODUCTION_RULES[68].add(GREATER_EQUAL_THAN);

    // 69 - RELOP
    PRODUCTION_RULES[69].add(EQUAL);

    // 70 - RELOP
    PRODUCTION_RULES[70].add(NOT_EQUAL);

    // 71 - ARITHMETIC_EXPRESSION
    PRODUCTION_RULES[71].add(TERM);
    PRODUCTION_RULES[71].add(ARITHMETIC_EXPRESSION_PRIME);

    // 72 - ARITHMETIC_EXPRESSION_PRIME
    PRODUCTION_RULES[72].add(ADDOP);
    PRODUCTION_RULES[72].add(TERM);
    PRODUCTION_RULES[72].add(ARITHMETIC_EXPRESSION_PRIME);

    // 73 - ARITHMETIC_EXPRESSION_PRIME *PRODUCES EPSILON*

    // 74 - ADDOP
    PRODUCTION_RULES[74].add(ADDITION);

    // 75 - ADDOP
    PRODUCTION_RULES[75].add(SUBSTRACTION);

    // 76 - TERM 
    PRODUCTION_RULES[76].add(FACTOR);
    PRODUCTION_RULES[76].add(TERM_PRIME);

    // 77 - TERM_PRIME
    PRODUCTION_RULES[77].add(MULOP);
    PRODUCTION_RULES[77].add(FACTOR);
    PRODUCTION_RULES[77].add(TERM_PRIME);

    // 78 - TERM_PRIME *PRODUCES EPSILON*

    // 79 - MULOP
    PRODUCTION_RULES[79].add(MULTIPLICATION);
    
    // 80 - MULOP
    PRODUCTION_RULES[80].add(DIVISION);

    // 81 - FACTOR
    PRODUCTION_RULES[81].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[81].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[81].add(CLOSE_PARENTHESIS);

    // 82 - FACTOR
    PRODUCTION_RULES[82].add(IDENTIFIER);
    PRODUCTION_RULES[82].add(FACTOR_PRIME);

    // 83 - FACTOR
    PRODUCTION_RULES[83].add(NUMBER);

    // 84 - FACTOR_PRIME *PRODUCES EPSILON*

    // 85 - FACTOR_PRIME
    PRODUCTION_RULES[85].add(OPEN_SBRACKET);
    PRODUCTION_RULES[85].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[85].add(CLOSE_SBRACKET);

    // 86 - FACTOR_PRIME
    PRODUCTION_RULES[86].add(OPEN_PARENTHESIS);
    PRODUCTION_RULES[86].add(ARGS);
    PRODUCTION_RULES[86].add(CLOSE_PARENTHESIS);

    // 87 - ARGS
    PRODUCTION_RULES[87].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[87].add(ARGS_LIST_PRIME);

    // 88 - ARGS *PRODUCES EPSILON*

    // 89 - ARGS_LIST_PRIME
    PRODUCTION_RULES[89].add(COMMA);
    PRODUCTION_RULES[89].add(ARITHMETIC_EXPRESSION);
    PRODUCTION_RULES[89].add(ARGS_LIST_PRIME);

    // 90 - ARGS_LIST_PRIME *PRODUCES EPSILON*
  }

  // Bidimensional array used to represent the LL(1) Parsing Table.
  public static int[][] PARSING_TABLE = new int[][]
  { /* ID,   NUM,     /,    *,    +,    -,    <,   <=,    >,   >=,    =,   ==,   !=,    ;,    ,,    (,    ),    [,    ],    {,    }, else,   if,  int, return, void, while, input, output,    $ */
    { ER1,	 ER1,	  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1,  ER1, ER1,   ER1,  ER1,  ER1,	  0,	  ER1,	  0,	 ER1,   ER1,    ER1,  ER1  }, /* PROGRAM */
    { ER2,	 ER2,	  ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,	ER2,  ER2,  ER2,  ER2,  ER2,  ER2,  ER2,  ER2,  ER2,    1,    ER2,    1,   ER2,   ER2,    ER2,	  2  }, /* DECLARATION_LIST_PRIME */
    { ER3,	 ER3,	  ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,  ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	ER3,	  3,	  ER3,	  4,	 ER3,	  ER3,	  ER3,	ER3  }, /* DECLARATION */
    { ER4,	 ER4,	  ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	  5,	ER4,	  7,	ER4,	  6,	ER4,	ER4,	ER4,	ER4,	ER4,	ER4,	  ER4,	ER4,	 ER4,	  ER4,	  ER4,	ER4  }, /* DECLARATION_PRIME */
    { ER5,	 ER5,	  ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	ER5,	  8,	ER5,	ER5,	ER5,	ER5,	  ER5,	ER5,	 ER5,	  ER5,	  ER5,	ER5  }, /* DECLARATION_BIPRIME */
    { ER6,	 ER6,	  ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	ER6,	  9,	ER6,	ER6,	ER6,	   10,	ER6,	 ER6,	  ER6,	  ER6,	ER6  }, /* DECLARATION_TRIPRIME */
    { ER7,	 ER7,	  ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	ER7,	 11,	  ER7,	ER7,	 ER7,	  ER7,	  ER7,	ER7  }, /* VAR_DECLARATION */
    { ER8,	 ER8,	  ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	 12,	ER8,	ER8,	ER8,	 13,	ER8,	ER8,	ER8,	ER8,	ER8,	ER8,	  ER8,	ER8,	 ER8,	  ER8,	  ER8,	ER8  }, /* VAR_DECLARATION_PRIME */
    { ER9,	 ER9,	  ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	ER9,	 14,	  ER9,	 15,	 ER9,	  ER9,	  ER9,	ER9  }, /* PARAMS */
    {ER10,	ER10,	 ER10, ER10, ER10, ER10, ER10, ER10, ER10, ER10, ER10, ER10, ER10, ER10,	 16, ER10,	 17, ER10, ER10, ER10, ER10, ER10, ER10, ER10,	 ER10, ER10,	ER10,	 ER10,	 ER10, ER10  }, /* PARAM_LIST_PRIME */
    {ER11,	ER11,	 ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11, ER11,	 18,	 ER11, ER11,	ER11,	 ER11,	 ER11, ER11  }, /* PARAM */
    {ER12,	ER12,	 ER12, ER12, ER12, ER12, ER12, ER12, ER12, ER12, ER12, ER12, ER12, ER12,	 19, ER12,	 19,	 20, ER12, ER12, ER12, ER12, ER12, ER12,	 ER12, ER12,	ER12,	 ER12,	 ER12, ER12  }, /* PARAM_PRIME */
    {  22,	ER13,	 ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13, ER13,	 21, ER13, ER13,	 23, ER13,	   27, ER13,	  24,	   25,	   26, ER13  }, /* COMPOUND_STMT */
    { ER14,	ER14,	 ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14, ER14,	 29, ER14, ER14, ER14,	   28, ER14,	ER14,	 ER14,	 ER14, ER14  }, /* COMPOUND_STMT_PRIME */
    { ER15,	ER15,	 ER15, ER15, ER15, ER15, ER15, ER15, ER15, ER15,	 30, ER15, ER15, ER15, ER15,	 32, ER15,	 31, ER15, ER15, ER15, ER15, ER15, ER15,	 ER15, ER15,	ER15,	 ER15,	 ER15, ER15  }, /* COMPOUND_STMT_BIPRIME */
    {  33,	ER16,	 ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16, ER16,	 34,	 33, ER16,	   33, ER16,	  33,	   33,	   33, ER16  }, /* COMPOUND_STMT_TRIPRIME */
    {  36,	  36,	 ER17, ER17, ER17, ER17, ER17, ER17, ER17, ER17, ER17, ER17, ER17,	 35, ER17,	 36, ER17, ER17, ER17, ER17, ER17, ER17, ER17, ER17,	 ER17, ER17,  ER17,	 ER17,	 ER17, ER17  }, /* COMPOUND_STMT_POLYPRIME */
    { ER18,	ER18,	 ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18, ER18,	 37, ER18, ER18, ER18, ER18,	 ER18, ER18,	ER18,	 ER18,	 ER18, ER18  }, /* COMPOUND_STMT_RET */
    {  39,	ER19,	 ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19, ER19,	 39, ER19,	 39,	 38,	   39, ER19,	  39,	   39,	   39, ER19  }, /* LOCAL_DECLARATIONS */
    {  41,	ER20,	 ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20, ER20,	 41, ER20,	 41,	 40,	   41, ER20,	  41,	   41,	   41, ER20  }, /* LOCAL_DECLARATIONS_PRIME */
    {  42,	ER21,	 ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21, ER21,	 43, ER21,	 42, ER21,	   43, ER21,	  42,	   42,	   42, ER21  }, /* STATEMENT_LIST */
    {  44,	ER22,	 ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22, ER22,	 45, ER22,	 44, ER22,	   45, ER22,	  44,	   44,	   44, ER22  }, /* STATEMENT_LIST_PRIME */
    {  46,	ER23,	 ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23, ER23,	 47, ER23,	 ER23, ER23,	  48,	   49,	   50, ER23  }, /* STATEMENT */
    { ER24,	ER24,	 ER24, ER24, ER24, ER24, ER24, ER24, ER24, ER24,	 51, ER24, ER24, ER24, ER24,	 53, ER24,	 52, ER24, ER24, ER24, ER24, ER24, ER24,	 ER24, ER24,	ER24,	 ER24,	 ER24, ER24  }, /* STATEMENT_PRIME */
    {  54,	ER25,	 ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25, ER25,	 54,	 55,	 54, ER25,	   54, ER25,	  54,	   54,	   54, ER25  }, /* STATEMENT_BIPRIME */
    { ER26,	ER26,	 ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26, ER26,	   56, ER26,	ER26,	 ER26,	 ER26, ER26  }, /* RETURN_STMT */
    {  58,	  58,	 ER27, ER27, ER27, ER27, ER27, ER27, ER27, ER27, ER27, ER27, ER27,	 57, ER27,	 58, ER27, ER27, ER27, ER27, ER27, ER27, ER27, ER27,	 ER27, ER27,	ER27,	 ER27,	 ER27, ER27  }, /* RETURN_STMT_PRIME */
    {  59,	ER28,	 ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28, ER28,	 ER28, ER28,	ER28,	 ER28,	 ER28, ER28  }, /* VAR */
    { ER29,	ER29,	 ER29, ER29, ER29, ER29, ER29, ER29, ER29, ER29, ER29, ER29, ER29,	 60, ER29, ER29, ER29,	 61, ER29, ER29, ER29, ER29, ER29, ER29,	 ER29, ER29,	ER29,	 ER29,	 ER29, ER29  }, /* VAR_PRIME */
    {  62,	  62,	 ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30,	 62, ER30, ER30, ER30, ER30, ER30, ER30, ER30, ER30,	 ER30, ER30,	ER30,	 ER30,	 ER30, ER30  }, /* EXPRESSION */
    { ER31,	ER31,	 ER31, ER31, ER31, ER31,	 63,	 63,	 63,	 63, ER31,	 63,	 63,	 64, ER31, ER31,	 64, ER31, ER31, ER31, ER31, ER31, ER31, ER31,	 ER31, ER31,	ER31,	 ER31,	 ER31, ER31  }, /* EXPRESSION_PRIME */
    { ER32,	ER32,	 ER32, ER32, ER32, ER32,	 66,	 65,	 67,	 68, ER32,	 69,	 70, ER32, ER32, ER32, ER32, ER32, ER32, ER32, ER32, ER32, ER32, ER32,	 ER32, ER32,	ER32,	 ER32,	 ER32, ER32  }, /* RELOP */
    {  71,	  71,	 ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33,	 71, ER33, ER33, ER33, ER33, ER33, ER33, ER33, ER33,	 ER33, ER33,	ER33,	 ER33,	 ER33, ER33  }, /* ARITHMETIC_EXPRESSION */
    { ER34,	ER34,	 ER34, ER34,	 72,   72,	 73,	 73,	 73,	 73, ER34,	 73,	 73,	 73,	 73, ER34,	 73, ER34,	 73, ER34, ER34, ER34, ER34, ER34,	 ER34, ER34,	ER34,	 ER34,	 ER34, ER34  }, /* ARITHMETIC_EXPRESSION_PRIME */
    { ER35,	ER35,	 ER35, ER35,	 74,   75, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35, ER35,	 ER35, ER35,	ER35,	 ER35,	 ER35, ER35  }, /* ADDOP */
    {  76,	  76,	 ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36,	 76, ER36, ER36, ER36, ER36, ER36, ER36, ER36, ER36,	 ER36, ER36,	ER36,	 ER36,	 ER36, ER36  }, /* TERM */
    { ER37,	ER37,	   77,	 77,	 78,   78,	 78,	 78,	 78,	 78, ER37,	 78,	 78,	 78,	 78, ER37,	 78, ER37,	 78, ER37, ER37, ER37, ER37, ER37,	 ER37, ER37,	ER37,	 ER37,	 ER37, ER37  }, /* TERM_PRIME */
    { ER38,	ER38,	   80,	 79, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38, ER38,	 ER38, ER38,	ER38,	 ER38,	 ER38, ER38  }, /* MULOP */
    {  82,	  83,	 ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39,	 81, ER39, ER39, ER39, ER39, ER39, ER39, ER39, ER39,	 ER39, ER39,	ER39,	 ER39,	 ER39, ER39  }, /* FACTOR */
    { ER40,	ER40,	   84,	 84,	 84,   84,	 84,	 84,	 84,	 84, ER40,	 84,	 84,	 84,	 84,	 86,	 84,	 85,	 84, ER40, ER40, ER40, ER40, ER40,	 ER40, ER40,	ER40,	 ER40,	 ER40, ER40  }, /* FACTOR_PRIME */
    {  87,	  87,	 ER41, ER41, ER41, ER41, ER41, ER41, ER41, ER41, ER41, ER41, ER41, ER41, ER41,	 87,	 88, ER41, ER41, ER41, ER41, ER41, ER41, ER41,	 ER41, ER41,	ER41,	 ER41,	 ER41, ER41  }, /* ARGS */
    { ER42,	ER42,	 ER42, ER42, ER42, ER42, ER42, ER42, ER42, ER42, ER42, ER42, ER42, ER42,	 89, ER42,	 90, ER42, ER42, ER42, ER42, ER42, ER42, ER42,	 ER42, ER42,	ER42,	 ER42,	 ER42, ER42  }  /* ARGS_LIST_PRIME */
  };

  // Array used to determine the type of token that triggers an error based on its token id.
  public static final String[] TOKENS = new String[]
  {
    "ID",
    "NUM",
    "/",
    "*",
    "+",
    "-",
    "<",
    "<=",
    ">",
    ">=",
    "=",
    "==",
    "!=",
    ";",
    ",",
    "(",
    ")",
    "[",
    "]",
    "{",
    "}",
    "else",
    "if",
    "int",
    "return",
    "void",
    "while",
    "input",
    "output",
    "$"
  };

  // Method used to get the error messages' description given its error code and the token that triggered the error.
  public static String getErrorDescription(int errorCode, int currentToken, int readToken) {
    String errorMessage = "";
    String currentTokenChar = TOKENS[readToken];
    switch(errorCode) {
      case ER1:
        errorMessage = String.format(ERM1, currentTokenChar);
        break;
      case ER2:
        errorMessage = String.format(ERM2, currentTokenChar);
        break;
      case ER3:
        errorMessage = String.format(ERM3, currentTokenChar);
        break;
      case ER4:
        errorMessage = String.format(ERM4, currentTokenChar);
        break;
      case ER5:
        errorMessage = String.format(ERM5, currentTokenChar);
        break;
      case ER6:
        errorMessage = String.format(ERM6, currentTokenChar);
        break;
      case ER7:
        errorMessage = String.format(ERM7, currentTokenChar);
        break;
      case ER8:
        errorMessage = String.format(ERM8, currentTokenChar);
        break;
      case ER9:
        errorMessage = String.format(ERM9, currentTokenChar);
        break;
      case ER10:
        errorMessage = String.format(ERM10, currentTokenChar);
        break;
      case ER11:
        errorMessage = String.format(ERM11, currentTokenChar);
        break;
      case ER12:
        errorMessage = String.format(ERM12, currentTokenChar);
        break;
      case ER13:
        errorMessage = String.format(ERM13, currentTokenChar);
        break;
      case ER14:
        errorMessage = String.format(ERM14, currentTokenChar);
        break;
      case ER24:
      case ER15:
        errorMessage = String.format(ERM15, currentTokenChar);
        break;
      case ER16:
        errorMessage = String.format(ERM16, currentTokenChar);
        break;
      case ER17:
        errorMessage = String.format(ERM17, currentTokenChar);
        break;
      case ER18:
        errorMessage = String.format(ERM18, currentTokenChar);
        break;
      case ER19:
      case ER20:
        errorMessage = String.format(ERM19, currentTokenChar);
        break;
      case ER21:
      case ER22:
        errorMessage = String.format(ERM20, currentTokenChar);
        break;
      case ER23:
        errorMessage = String.format(ERM21, currentTokenChar);
        break;
      case ER25:
        errorMessage = String.format(ERM22, currentTokenChar);
        break;
      case ER26:
        errorMessage = String.format(ERM23, currentTokenChar);;
        break;
      case ER27:
        errorMessage = String.format(ERM24, currentTokenChar);
        break;
      case ER28:
        errorMessage = String.format(ERM25, currentTokenChar);
        break;
      case ER29:
        errorMessage = String.format(ERM26, currentTokenChar);
        break;
      case ER30: 
        errorMessage = String.format(ERM27, currentTokenChar);
        break;
      case ER31:
        errorMessage = String.format(ERM28, currentTokenChar);
        break;
      case ER32:
        errorMessage = String.format(ERM29, currentTokenChar);
        break;
      case ER33:
      case ER36:
      case ER39:
        errorMessage = String.format(ERM30, currentTokenChar);
        break;
      case ER34:
        errorMessage = String.format(ERM31, currentTokenChar);
        break;
      case ER35:
        errorMessage = String.format(ERM32, currentTokenChar);
        break;
      case ER37:
        errorMessage = String.format(ERM33, currentTokenChar);
        break;
      case ER38:
        errorMessage = String.format(ERM34, currentTokenChar);
        break;
      case ER40:
        errorMessage = String.format(ERM35, currentTokenChar);
        break;
      case ER41:
        errorMessage = String.format(ERM36, currentTokenChar);
        break;
      case ER42:
        errorMessage = String.format(ERM37, currentTokenChar);
        break;
      default:
        errorMessage = "ERROR: Syntactical error found";
        break;
    }

    return errorMessage + String.format(". To correct the error look at the token #%s", currentToken);
  }

  // Method used to initialize either the identifiers' semantic symbol table or the numbers' semantic symbol table with the
  // size of their equivalents obtained during the lexical analysis.
  public static void initializeSemantics(int identifierSymbolsTableSize, int numberSymbolsTableSize) {
    IDENTIFIERS_SEMANTIC_SYMBOL_TABLE = new int[identifierSymbolsTableSize][2];
    NUMBERS_SEMANTIC_SYMBOL_TABLE = new int[numberSymbolsTableSize];
  }

  // Method used to assign the corresponding semantic tags to any entrance within the identifiers' semantic symbol table.
  private static void setIdentifierSemantics(int entry, int position, int description) {
    IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[entry][position] = description;
  }

  // Method used to assign the corresponding semantic tags to any entrance within the numbers' semantic symbol table.
  private static void setNumberSemantic(int entry, int description) {
    NUMBERS_SEMANTIC_SYMBOL_TABLE[entry] = description;
  }

  // Method used to assign the proper semantic tag (according to token type) to a given entry of the identifiers' symbol
  // table. 
  public static void assignTokenTypeToIdentifiers(Object[] token, Object[] nextToken) {

    // Retrieve the pointer to the entry to the identifiers' symbol table from the current token.
    int symbolTableEntry = (int) token[1];

    // If the current token is an identifier and the next available token is a parenthesis, then evaluate the identifier as a
    // function name.
    if((int) token[0] == IDENTIFIER && (int) nextToken[0] == OPEN_PARENTHESIS) {

      // If the identifier type has not been defined, then assign a tag to indicate that the identifier is a function name.
      if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][TOKEN_TYPE] == UNDEFINED) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, TOKEN_TYPE, FUNCTION_TYPE);
      }

      // If the identifier type has been previously defined as a variable name, then assign a new tag to indicate that the
      // identifier is a function and a variable name.
      else if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][TOKEN_TYPE] == VARIABLE_TYPE) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, TOKEN_TYPE, VARIABLE_AND_FUNCTION_TYPE);
      }  
    } 
    
    // If the identifier cannot be evaluated as a function name.
    else {

      // If the identifier type has not been defined, then assign a tag to indicate that the identifier is a variable name.
      if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][TOKEN_TYPE] == UNDEFINED) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, TOKEN_TYPE, VARIABLE_TYPE);
      }

      // If the identifier type has been previously defined as a function name, then assign a new tag to indicate that the 
      // identifier is a function and a variable name.
      else if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][TOKEN_TYPE] == FUNCTION_TYPE) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, TOKEN_TYPE, VARIABLE_AND_FUNCTION_TYPE);
      }
    }
  }

  // Method used to assign the proper semantic tag (according to token scope) to a given entry of the identifiers'
  // symbol table.
  public static void assignTokenScopeToIdentifiers(Object[] token, int previousRule) {

    // Retrieve the pointer to the entry to the identifiers' symbol table from the current token.
    int symbolTableEntry = (int) token[1];

    // If the identifier is not get from a global, local or param declaration rule of the Context Free Grammar, then exit the 
    // function. A scope is only assigned to a variable in the moment it is declared. 
    if (previousRule != DECLARATION && previousRule != VAR_DECLARATION && previousRule != PARAM) {
      return;
    }
    
    // If the current token is being compared to the appereance of an identifier in a global declaration rule of the Context Free Grammar.
    if (previousRule == DECLARATION) {

      // If the identifier scope has not been defined, then assign a tag to indicate that the identifier scope is global.
      if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][IDENTIFIER_SCOPE] == UNDEFINED) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, IDENTIFIER_SCOPE, GLOBAL_SCOPE);
      }

      // If the identifier scope has been previously defined as local, then assign a new tag to indicate that the identifier scope is
      // global and local.
      else if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][IDENTIFIER_SCOPE] == LOCAL_SCOPE) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, IDENTIFIER_SCOPE, LOCAL_AND_GLOBAL_SCOPE);
      }  
    }

    // If the current token is being compared to the appereance of an identifier in a local or param declaration rule of the Context Free Grammar.
    else {

      // If the identifier scope has not been defined, then assign a tag to indicate that the identifier scope is local.
      if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][IDENTIFIER_SCOPE] == UNDEFINED) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, IDENTIFIER_SCOPE, LOCAL_SCOPE);
      }

      // If the identifier scope has been previously defined as global, then assign a new tag to indicate that the identifier scope is
      // global and local.
      else if(IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[symbolTableEntry][IDENTIFIER_SCOPE] == GLOBAL_SCOPE) {
        SyntacticEnvironment.setIdentifierSemantics(symbolTableEntry, IDENTIFIER_SCOPE, LOCAL_AND_GLOBAL_SCOPE);
      } 
    }
  }

  // Method used to assign the proper semantic tag (according to token type) to a given entry of the numbers'
  // symbol table. For the syntactic analyzer being developed, the only available token type for numeric values 
  // is an integer constant.
  // constant
  public static void assignTokenTypeToNumbers(Object[] token) {
    SyntacticEnvironment.setNumberSemantic((int) token[1], INTEGER_CONSTANT);
  }

  // Method used to obtain the whole semantic symbol table for identifiers.
  public static int getIdentifiersSemanticSymbolTable(int index, int category) {
    return IDENTIFIERS_SEMANTIC_SYMBOL_TABLE[index][category];
  }

  // Method used to obtain the whole semantic symbol table for numbers.
  public static int getNumbersSemanticSymbolTable(int index) {
    return NUMBERS_SEMANTIC_SYMBOL_TABLE[index];
  }

  // Method used to print either the updated symbols' table for identifiers or numbers.
  public static void printSymbolsTable(Object[][] symbolsTable, int type) {

    // If the symbols' table to be printed is for the identifiers.
    if (type == IDENTIFIERS_SYMBOL_TABLE_NUMBER) {
      System.out.println("IDENTIFIERS SYMBOL TABLE UPDATED");
      System.out.println("Identifier => column 1. \nIdentifier type => column 2. \nIdentifier scope => column 3.");
      System.out.println("- Identifier type. \n--- 1 => Variable. \n--- 2 => Function. \n--- 3 => Variable/function");
      System.out.println("- Identifier scope. \n--- 1 => Local. \n--- 2 => Global. \n--- 3 => Local/global");
      for (int i=0; i<symbolsTable.length; i++) {
        System.out.println(String.format("%s %s %s", symbolsTable[i][0], symbolsTable[i][1], symbolsTable[i][2]));
      }
    }

    // If the symbols' table to be printed is for the numbers.
    else {
      System.out.println("NUMBERS SYMBOL TABLE UPDATED");
      System.out.println("Number => column 1. \nNumber type => column 2");
      System.out.println("- Number type. \n--- 1 => Integer");
      for (int i=0; i<symbolsTable.length; i++) {
        System.out.println(String.format("%s %s", symbolsTable[i][0], symbolsTable[i][1]));
      }
    }
    
  }
  
}
