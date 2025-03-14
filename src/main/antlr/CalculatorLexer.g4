// DELETE THIS CONTENT IF YOU PUT COMBINED GRAMMAR IN Parser TAB
lexer grammar CalculatorLexer;

@header {
package org.example;
}

SUM : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;
LPAREN : '(' ;
RPAREN : ')' ;
POW : '^' ;
MOD : '%';
FLOOR : '_' ;

NUMBER  : [0-9]+
        | [0-9]+ '.' [0-9]+
        | '.' [0-9]+
        ;
WS: [ \t\n\r\f]+ -> skip ;

