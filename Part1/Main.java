import java.io.IOException;
import java.util.*;

public class Main { 
    public static void main(String[] args) {
	try {
            System.out.println((new CalculatorParser(System.in)).eval());
        } catch (IOException | ParseError e) {
            System.err.println(e.getMessage());
        }
    }
}

