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
MOD : '%' ;


INT : [0-9]+ ;
FLOAT : [0-9]+ '.' [0-9]+ ;
NUMBER  : INT | FLOAT ;
WS: [ \t\n\r\f]+ -> skip ;

