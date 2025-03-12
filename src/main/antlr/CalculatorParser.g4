parser grammar CalculatorParser;
options { tokenVocab=CalculatorLexer; }

@header {
package org.example;
}

start
    : expression EOF
    ;

expression
    : NUMBER
    | SUB right=expression
    | LPAREN inner=expression RPAREN
    | left=expression operator=POW right=expression
    | left=expression operator=(MUL|DIV) right=expression
    | left=expression operator=(SUM|SUB) right=expression
    ;
