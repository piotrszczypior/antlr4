package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        CalculatorVisitor visitor = new CalculatorVisitor();

        while (true) {
            System.out.print(">> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // TODO:
                CalculatorLexer lexer = new CalculatorLexer(CharStreams.fromString(input));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                CalculatorParser parser = new CalculatorParser(tokens);
                ParseTree tree = parser.start();  // Entry rule

                Double result = visitor.visit(tree);
                System.out.println("Wynik: " + result);
            } catch (Exception e) {
                System.out.println("Błąd: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
