package org.pwr.compiler;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.pwr.grammar.FirstBaseVisitor;
import org.pwr.grammar.FirstLexer;
import org.pwr.grammar.FirstParser;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;


public class EmitVisitor extends FirstBaseVisitor<ST> {

    private final STGroup stGroup;

    private int ifIdentifier = 0;

    private int equalIdentifier = 0;

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

//    @Override
//    public ST visitTerminal(TerminalNode node) {
//        return new ST("Terminal node:<n>").add("n", node.getText());
//    }

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
    public ST visitArithmeticOperationStatement(FirstParser.ArithmeticOperationStatementContext ctx) {

        return switch (ctx.operation.getType()) {
            case FirstLexer.SUB -> buildOperationTemplate(stGroup.getInstanceOf("subtraction"), ctx);
            case FirstLexer.ADD -> buildOperationTemplate(stGroup.getInstanceOf("addition"), ctx);
            case FirstLexer.MUL -> buildOperationTemplate(stGroup.getInstanceOf("multiplication"), ctx);
            case FirstLexer.DIV -> buildOperationTemplate(stGroup.getInstanceOf("division"), ctx);
            default -> throw new IllegalStateException("Unexpected arithmetic statement: " + ctx.operation.getType());
        };
    }

    @Override
    public ST visitLogicOperationStatement(FirstParser.LogicOperationStatementContext ctx) {

        return getLogicOperationTemplate(ctx)
                .add("p1", visit(ctx.left))
                .add("p2", visit(ctx.right));
    }

    @Override
    public ST visitIfStatement(FirstParser.IfStatementContext ctx) {
        ST ifTemplate = stGroup.getInstanceOf("if")
                .add("id", ifIdentifier++)
                .add("condition", visit(ctx.expr()))
                .add("thenStatement", visit(ctx.block().getFirst()));

        if (ctx.block().size() > 1) {
            ifTemplate.add("elseStatement", visit(ctx.block().getLast()));
        }
        return ifTemplate;
    }


    private ST getLogicOperationTemplate(FirstParser.LogicOperationStatementContext ctx) {
        return switch (ctx.operation.getType()) {
            case FirstLexer.EQ -> stGroup.getInstanceOf("comparisonEqual").add("id", equalIdentifier++);
            case FirstLexer.NEQ -> stGroup.getInstanceOf("comparisonNotEqual");

            default -> throw new IllegalStateException("Unexpected logic statement: " + ctx.operation.getType());
        };
    }


    private ST buildOperationTemplate(ST template, FirstParser.ArithmeticOperationStatementContext ctx) {
        return template.add("p1", visit(ctx.left)).add("p2", visit(ctx.right));
    }
}
