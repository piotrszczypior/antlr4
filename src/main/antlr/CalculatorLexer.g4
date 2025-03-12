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

NUMBER : [0-9]+ ;
WS: [ \t\n\r\f]+ -> skip ;

