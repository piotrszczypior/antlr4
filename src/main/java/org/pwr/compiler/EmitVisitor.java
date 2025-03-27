package org.pwr.compiler;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.pwr.grammar.FirstBaseVisitor;
import org.pwr.grammar.FirstLexer;
import org.pwr.grammar.FirstParser;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;


public class EmitVisitor extends FirstBaseVisitor<ST> {

    private final STGroup stGroup;

    public EmitVisitor(STGroup group) {
        super();
        this.stGroup = group;
    }

    @Override
    protected ST defaultResult() {
        return stGroup.getInstanceOf("deflt");
    }

    @Override
    protected ST aggregateResult(ST aggregate, ST nextResult) {
        if (nextResult != null) {
            aggregate.add("elem", nextResult);
        }
        return aggregate;
    }

    @Override
    public ST visitTerminal(TerminalNode node) {
        return new ST("Terminal node:<n>").add("n", node.getText());
    }

    @Override
    public ST visitIntStatement(FirstParser.IntStatementContext ctx) {
        ST st = stGroup.getInstanceOf("int");
        st.add("i", ctx.INT().getText());
        return st;
    }

    @Override
    public ST visitIdStatement(FirstParser.IdStatementContext ctx) {

        return stGroup.getInstanceOf("variable")
                .add("i", ctx.ID().getText());
    }

    @Override
    public ST visitAssign(FirstParser.AssignContext ctx) {

        return stGroup.getInstanceOf("assignment")
                .add("name", ctx.ID().getText())
                .add("value", visit(ctx.expr()));
    }

    @Override
    public ST visitVarStatement(FirstParser.VarStatementContext ctx) {
        if (ctx.expr() != null) {
            return stGroup.getInstanceOf("declarationWithAssignment")
                    .add("name", ctx.ID().getText())
                    .add("value", visit(ctx.expr()));
        }
        return stGroup.getInstanceOf("declaration")
                .add("name", ctx.ID().getText());
    }

    @Override
    public ST visitParentheses(FirstParser.ParenthesesContext ctx) {
        return this.visit(ctx);
    }

    @Override
    public ST visitOperationStatement(FirstParser.OperationStatementContext ctx) {

        return switch (ctx.operation.getType()) {
            case FirstLexer.SUB -> buildOperationTemplate(stGroup.getInstanceOf("subtraction"), ctx);
            case FirstLexer.ADD -> buildOperationTemplate(stGroup.getInstanceOf("addition"), ctx);
            case FirstLexer.MUL -> buildOperationTemplate(stGroup.getInstanceOf("multiplication"), ctx);
            case FirstLexer.DIV -> buildOperationTemplate(stGroup.getInstanceOf("division"), ctx);
            default -> stGroup.getInstanceOf("operation");
        };
    }

    private ST buildOperationTemplate(ST template, FirstParser.OperationStatementContext ctx) {
        return template.add("p1", visit(ctx.left)).add("p2", visit(ctx.right));
    }
}
