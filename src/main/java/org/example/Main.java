package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {

    public static void main(String[] args) throws Exception {
        //TIP create a CharStream that reads from standard input
//        CharStream input = CharStreams.fromStream(System.in);
        CharStream input = CharStreams.fromFileName("input.txt");

        //TIP create a lexer that feeds off of input CharStream
        ExprLexer lexer = new ExprLexer(input);

        //TIP create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //TIP create a parser that feeds off the tokens buffer
        ExprParser parser = new ExprParser(tokens);

        //TIP start parsing at the program rule
        ParseTree tree = parser.program();
        // System.out.println(tree.toStringTree(parser));

        //TIP create a visitor to traverse the parse tree
        org.example.LogicVisitor visitor = new org.example.LogicVisitor();
        System.out.println(visitor.visit(tree));
    }
}
