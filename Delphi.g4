grammar Delphi;

program: 'program' IDENTIFIER ';' block '.' ;

block: (classDeclaration | interfaceDeclaration | statement)* ;

classDeclaration: 'class' IDENTIFIER ('extends' IDENTIFIER)? ('implements' IDENTIFIER)? '{' classBody '}' ;

interfaceDeclaration: 'interface' IDENTIFIER '{' interfaceBody? '}' ;

interfaceBody: methodSignature+ ;

methodSignature: 'procedure' IDENTIFIER '(' parameterList? ')' ';' ;

parameterList: IDENTIFIER ':' IDENTIFIER (',' IDENTIFIER ':' IDENTIFIER)* ;

classBody: (variableDeclaration | methodDeclaration | constructorDeclaration | destructorDeclaration)* ;

variableDeclaration: visibilitySpecifier IDENTIFIER ':' IDENTIFIER ';' ;

constructorDeclaration: 'constructor' IDENTIFIER '(' parameterList? ')' ';' ;

destructorDeclaration: 'destructor' IDENTIFIER '(' ')' ';' ;

methodDeclaration: visibilitySpecifier* 'procedure' IDENTIFIER '(' parameterList? ')' ';' ;

visibilitySpecifier: 'public' | 'private' | 'protected' ;

statement: integerOutput ;

integerOutput: 'print' '(' INT ')' ';' ;

IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]* ;
INT: [0-9]+ ;
WS: [ \t\r\n]+ -> skip ;
