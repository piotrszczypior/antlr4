package org.pwr.compiler;

import org.pwr.grammar.FirstBaseVisitor;
import org.pwr.grammar.FirstLexer;
import org.pwr.grammar.FirstParser;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.List;


public class EmitVisitor extends FirstBaseVisitor<ST> {

    private final STGroup stGroup;

    private int ifIdentifier = 0;

    private int logicJumpIdentifier = 0;

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
                .add("p2", visit(ctx.right))
                .add("id", logicJumpIdentifier++);
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

    @Override
    public ST visitDefStatement(final FirstParser.DefStatementContext ctx) {

        return stGroup.getInstanceOf("functionDefinition")
                .add("name", ctx.ID().getText())
                .add("parameters", ctx.params().ID())
                .add("numerOfParams", ctx.params().ID().size() + 1)
                .add("body", visit(ctx.block()));
    }

    @Override
    public ST visitDefCallStatement(final FirstParser.DefCallStatementContext ctx) {
        List<ST> argsTemplates = ctx.expr().stream().map(this::visit).toList();

        return stGroup.getInstanceOf("functionCall")
                .add("name", ctx.ID().getText())
                .add("parameters", argsTemplates);
    }

    @Override
    public ST visitNotStatement(final FirstParser.NotStatementContext ctx) {

        return stGroup.getInstanceOf("notStatement")
                .add("p1", visit(ctx.right))
                .add("id", logicJumpIdentifier++);
    }

    private ST getLogicOperationTemplate(FirstParser.LogicOperationStatementContext ctx) {
        return switch (ctx.operation.getType()) {
            case FirstLexer.EQ -> stGroup.getInstanceOf("comparisonEqual");
            case FirstLexer.NEQ -> stGroup.getInstanceOf("comparisonNotEqual");
            case FirstLexer.AND -> stGroup.getInstanceOf("andStatement");
            case FirstLexer.OR -> stGroup.getInstanceOf("orStatement");
            case FirstLexer.LT -> stGroup.getInstanceOf("lessThan");
            case FirstLexer.LTE -> stGroup.getInstanceOf("lessThanOrEqual");
            case FirstLexer.GT -> stGroup.getInstanceOf("greaterThan");
            case FirstLexer.GTE -> stGroup.getInstanceOf("greaterThanOrEqual");

            default -> throw new IllegalStateException("Unexpected logic statement: " + ctx.operation.getType());
        };
    }

    private ST buildOperationTemplate(ST template, FirstParser.ArithmeticOperationStatementContext ctx) {
        return template.add("p1", visit(ctx.left)).add("p2", visit(ctx.right));
    }
}
