public class Constants {
    
    public static final int[] UPPERCASE_LETTER = new int[] {65, 90};
    public static final int[] LOWERCASE_LETTER = new int[] {97, 122};
    public static final int[] NUMBER = new int[] {45, 57};
    public static final int PLUS = 43;
    public static final int MINUS = 45;
    public static final int MULTIPLIER = 42;
    public static final int SLASH = 47;
    public static final int LESS_THAN = 60;
    public static final int EQUAL = 61;
    public static final int GREATER_THAN = 62;
    public static final int ADMIRATION = 33;
    public static final int SEMICOLON = 59;
    public static final int COMMA = 44;
    public static final int[] PARENTHESES = new int[] {40, 41};
    public static final int[] SQUARE_BRACKETS = new int[] {91,93};
    public static final int[] CURLY_BRACKETS = new int[] {123, 125};


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
}
