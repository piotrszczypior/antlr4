package org.example;

public class CalculatorVisitor extends CalculatorParserBaseVisitor<Double> {

    private static final int NEGATIVE_OPERATOR = -1;

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
        if (CalculatorParser.MUL == ctx.operator.getType()) {
            return this.visit(ctx.left) * this.visit(ctx.right);
        }

        return this.visit(ctx.left) / this.visit(ctx.right);
    }

    @Override
    public Double visitParenthesesExpr(CalculatorParser.ParenthesesExprContext ctx) {
        return this.visit(ctx.inner);
    }

    @Override
    public Double visitModuloExpr(CalculatorParser.ModuloExprContext ctx) {
        return this.visit(ctx.left) % this.visit(ctx.right);
    }

    @Override
    public Double visitAddSubExpr(CalculatorParser.AddSubExprContext ctx) {
        if (CalculatorParser.SUM == ctx.operator.getType()) {
            return this.visit(ctx.left) + this.visit(ctx.right);
        }

        return this.visit(ctx.left) - this.visit(ctx.right);
    }

    @Override
    public Double visitUnaryMinusExpr(CalculatorParser.UnaryMinusExprContext ctx) {
        return NEGATIVE_OPERATOR * this.visit(ctx.right);
    }

    @Override
    public Double visitFloorExpr(CalculatorParser.FloorExprContext ctx) {
        return Math.floor(this.visit(ctx.left));
    }
}




