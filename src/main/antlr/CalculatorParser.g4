parser grammar CalculatorParser;
options { tokenVocab=CalculatorLexer; }

@header {
package org.example;
}

start
    : expression EOF
    ;

expression
    : NUMBER                                                # NumberExpr
    | SUB (right=expression)                                # UnaryMinusExpr
    | LPAREN inner=expression RPAREN                        # ParenthesesExpr
    | left=expression operator=POW right=expression       # PowerExpr
    | left=expression operator=(MUL|DIV) right=expression   # MulDivExpr
    | left=expression operator=(SUM|SUB) right=expression   # AddSubExpr
    | left=expression operator=MOD right=expression         # ModuloExpr
    | left=expression FLOOR                                     # FloorExpr
    ;
