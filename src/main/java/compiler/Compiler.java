import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {

    public static void main(String[] argv) {

        // Verify the number of arguments passed to the program.
        if(argv.length != 1){
            System.out.println("You must specify only the filename of your program");
            System.exit(1);
        }

       try {
            String sourceCode = readFile(argv[0]);
            System.out.println(sourceCode);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("The filename you specified does no exist");
            System.exit(1);
        } catch (IOException ioException) {
            System.out.println("There was a problem while reading the file");
            System.exit(1);
        }
    }

}
