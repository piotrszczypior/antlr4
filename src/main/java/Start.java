import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Start {
    private static final String TEST_FILE_NAME = "we.first";

    public static void main(String[] args) {
        CharStream inp;
        try {
            inp = CharStreams.fromFileName(TEST_FILE_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirstLexer lex = new FirstLexer(inp);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        FirstParser par = new FirstParser(tokens);

        ParseTree tree = par.prog();

        CalculatorVisitor v = new CalculatorVisitor(inp, tokens);
        v.visit(tree);
    }
}
