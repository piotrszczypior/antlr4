grammar First;

prog    : stat* EOF;

stat    : expr SEMICOLON                                                                    #exprStatement
        | IF_STATEMENT LBRACKET expr RBRACKET block (ELSE_STATEMENT block)?                 #ifStatement
        | WHILE_STATEMENT LBRACKET expr RBRACKET block                                      #whileStatement
        | PRINT_STATEMENT LBRACKET expr RBRACKET SEMICOLON                                  #printStatement
        | <assoc=right> VAR_STATEMENT ID (EQUAL_SIGN expr)?  SEMICOLON                      #varStatement
        | FOR_LOOP ID IN_RANGE LBRACKET expr RBRACKET block                                 #forStatemate
        ;

block   : stat                                                                              #singleBlock
        | LBRACE stat* RBRACE                                                               #bracketBlock
        ;

expr    : left=expr operation=(MUL|DIV) right=expr                                          #operationStatement
        | left=expr operation=(ADD|SUB) right=expr                                          #operationStatement
        | left=expr operation=(AND|OR|NOT|EQ|NEQ|LT|LTE|GT|GTE) right=expr                  #operationStatement
        | INT                                                                               #intStatement
        | LBRACKET expr RBRACKET                                                            #parentheses
        | <assoc=right> ID EQUAL_SIGN expr                                                  #assign
        | ID                                                                                #idStatement
        ;


SEMICOLON: ';';

PRINT_STATEMENT: 'print';
VAR_STATEMENT: 'var';

IF_STATEMENT : 'if' ;
ELSE_STATEMENT: 'else';
WHILE_STATEMENT: 'while';
FOR_LOOP: 'for';
IN_RANGE: 'in range';

LBRACKET : '(';
RBRACKET : ')';
LBRACE    : '{' ;
RBRACE    : '}' ;

EQUAL_SIGN: '=';

AND       : '&&' ;
OR        : '||' ;
NOT       : '!' ;
EQ        : '==' ;
NEQ       : '!=' ;
LT        : '<' ;
LTE       : '<=' ;
GT        : '>' ;
GTE       : '>=' ;

DIV : '/' ;
MUL : '*' ;
SUB : '-' ;
ADD : '+' ;

NEWLINE : [\r\n]+ -> channel(HIDDEN);
WS : [ \t]+ -> channel(HIDDEN) ;

INT     : [0-9]+ ;
ID : [a-zA-Z_][a-zA-Z0-9_]* ;

COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~'\n'* '\n' -> channel(HIDDEN) ;