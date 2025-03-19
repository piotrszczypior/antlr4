import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.TokenStream;

import java.util.HashMap;
import java.util.Map;

public class CalculatorVisitor extends FirstBaseVisitor<Integer> {

    private final Map<String, Integer> memory = new HashMap<>();

    private TokenStream tokStream;

    private CharStream input;

    public CalculatorVisitor(CharStream input) {
        this.input = input;
    }

    public CalculatorVisitor(TokenStream tokStream) {
        this.tokStream = tokStream;
    }

    public CalculatorVisitor(CharStream input, TokenStream tokStream) {
        this.input = input;
        this.tokStream = tokStream;
    }

    @Override
    public Integer visitProg(FirstParser.ProgContext ctx) {
        Integer result = 0;
        for (FirstParser.StatContext statCtx : ctx.stat()) {
            result = visit(statCtx);
        }
        return result;
    }

    @Override
    public Integer visitExprStatement(FirstParser.ExprStatementContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitIfStatement(FirstParser.IfStatementContext ctx) {
        Integer condition = visit(ctx.expr());
        if (condition != 0) {
            return visit(ctx.block(0));
        } else {
            if (ctx.block().size() > 1) {
                return visit(ctx.block(1));
            }
        }
        return 0;
    }

    @Override
    public Integer visitWhileStatement(FirstParser.WhileStatementContext ctx) {
        Integer result = 0;
        while (visit(ctx.expr()) != 0) {
            result = visit(ctx.block());
        }
        return result;
    }

    @Override
    public Integer visitPrintStatement(FirstParser.PrintStatementContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return value;
    }

    @Override
    public Integer visitVarStatement(FirstParser.VarStatementContext ctx) {
        String varName = ctx.ID().getText();
        Integer value = 0;
        if (ctx.expr() != null) {
            value = visit(ctx.expr());
        }

        memory.put(varName, value);
        return value;
    }

    @Override
    public Integer visitSingleBlock(FirstParser.SingleBlockContext ctx) {
        return visit(ctx.stat());
    }

    @Override
    public Integer visitBracketBlock(FirstParser.BracketBlockContext ctx) {
        Integer result = 0;
        for (FirstParser.StatContext statCtx : ctx.stat()) {
            result = visit(statCtx);
        }
        return result;
    }

    @Override
    public Integer visitParentheses(FirstParser.ParenthesesContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitIntStatement(FirstParser.IntStatementContext ctx) {
        return Integer.parseInt(ctx.getText());
    }

    @Override
    public Integer visitOperationStatement(FirstParser.OperationStatementContext ctx) {
        Integer left = visit(ctx.left);
        Integer right = visit(ctx.right);

        return switch (ctx.operation.getType()) {
            case FirstParser.ADD -> left + right;
            case FirstParser.SUB -> left - right;
            case FirstParser.MUL -> left * right;
            case FirstParser.DIV -> {
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield left / right;
            }
            case FirstParser.AND -> (left != 0 && right != 0) ? 1 : 0;
            case FirstParser.OR -> (left != 0 || right != 0) ? 1 : 0;
            case FirstParser.EQ -> (left.equals(right)) ? 1 : 0;
            case FirstParser.NEQ -> (!left.equals(right)) ? 1 : 0;
            case FirstParser.GT -> (left > right) ? 1 : 0;
            case FirstParser.GTE -> (left >= right) ? 1 : 0;
            case FirstParser.LT -> (left < right) ? 1 : 0;
            case FirstParser.LTE -> (left <= right) ? 1 : 0;
            default -> throw new ArithmeticException("Unknown operation type " + ctx.operation.getText());
        };
    }

    @Override
    public Integer visitAssign(FirstParser.AssignContext ctx) {
        String varName = ctx.ID().getText();
        Integer value = visit(ctx.expr());
        memory.put(varName, value);
        return value;
    }

    @Override
    public Integer visitIdStatement(FirstParser.IdStatementContext ctx) {
        if (!memory.containsKey(ctx.ID().getText())) {
            throw new RuntimeException("Unknown variable " + ctx.ID().getText());
        }

        return memory.get(ctx.ID().getText());
    }

    @Override
    public Integer visitForStatemate(FirstParser.ForStatemateContext ctx) {
        Integer result = 0;
        for (int i = 0; i < visit(ctx.expr()); i++) {
            result = visit(ctx.block());
        }

        return result;
    }
}
