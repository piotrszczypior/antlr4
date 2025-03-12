package org.example;

public class CalculatorVisitor extends CalculatorParserBaseVisitor<Double>{


    @Override
    public Double visitStart(CalculatorParser.StartContext ctx) {
        return super.visit(ctx.expression());
    }
    @Override
    public Double visitExpression(CalculatorParser.ExpressionContext ctx) {
        if (ctx.NUMBER() != null) {
            return Double.parseDouble(ctx.NUMBER().getText());
        }

        if (ctx.inner != null) {
            return visit(ctx.inner);
        }

        if (ctx.right != null) {
            return -visit(ctx.right);
        }

        if (ctx.operator != null) {
            double left = visit(ctx.left);
            double right = visit(ctx.right);

            switch (ctx.operator.getType()) {
                case CalculatorParser.POW:
                    return Math.pow(left, right);
                case CalculatorParser.MUL:
                    return left * right;
                case CalculatorParser.DIV:
                    if (right == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    return left / right;
                case CalculatorParser.SUM:
                    return left + right;
                case CalculatorParser.SUB:
                    return left - right;
            }
        }

        return 0.0;
    }
}




