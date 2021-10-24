package dk.rasmusbendix;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) {

        NumberCruncher cruncher = new NumberCruncher();
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while(true) {

            input = scanner.next();
            if(input.equalsIgnoreCase("-1"))
                break;

            System.out.println("Finding match for " + input);
            WordConstructor word = cruncher.findMatch(input);
            if(word != null)
                System.out.println("Found word match! " + word.getWord());

        }

    }

}
