package org.pwr.compiler;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.pwr.grammar.FirstLexer;
import org.pwr.grammar.FirstParser;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.FileWriter;
import java.io.IOException;


public class StartCompiler {

    private static final String TEST_FILE_NAME = "we.first";

    private static final String ST_FILE_PATH = "src/main/java/org/pwr/compiler/register.stg";

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

        // st group
        // STGroup.trackCreationEvents = true;
        STGroup group = new STGroupFile(ST_FILE_PATH);

        EmitVisitor em = new EmitVisitor(group);
        ST res = em.visit(tree);
        System.out.println(res.render());
        try {
            var wr = new FileWriter("wy.asm");
            wr.write(res.render());
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.inspect();
    }
}
