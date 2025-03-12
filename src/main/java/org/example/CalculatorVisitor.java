package org.example;

public class CalculatorVisitor extends CalculatorParserBaseVisitor<Double> {

    @Override
    public Double visitStart(CalculatorParser.StartContext ctx) {
        return this.visit(ctx.expression());
    }

    @Override
    public Double visitNumberExpr(CalculatorParser.NumberExprContext ctx) {
        return Double.parseDouble(ctx.NUMBER().getText());
    }

    @Override
    public Double visitPowerExpr(CalculatorParser.PowerExprContext ctx) {
        return Math.pow(this.visit(ctx.left), this.visit(ctx.right));
    }

    @Override
    public Double visitMulDivExpr(CalculatorParser.MulDivExprContext ctx) {
        if (ctx.operator.getText().equals("*")) {
            return this.visit(ctx.left) * this.visit(ctx.right);
        }

        return this.visit(ctx.left) / this.visit(ctx.right);
    }

    @Override
    public Double visitParenthesesExpr(CalculatorParser.ParenthesesExprContext ctx) {
        return this.visit(ctx.inner);
    }

    @Override
    public Double visitAddSubExpr(CalculatorParser.AddSubExprContext ctx) {
        if (ctx.operator.getText().equals("+")) {
            return this.visit(ctx.left) + this.visit(ctx.right);
        }

        return this.visit(ctx.left) - this.visit(ctx.right);
    }

    @Override
    public Double visitUnaryMinusExpr(CalculatorParser.UnaryMinusExprContext ctx) {
        return -1 * this.visit(ctx.right);
    }
}




