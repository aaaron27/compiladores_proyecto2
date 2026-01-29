package org.analizadorLexico;

import java_cup.runtime.Symbol;

%%

%class Lexer
%public
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
      return new Symbol(type, yyline, yycolumn, yytext());
    }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

  private void error(String message) {
    System.err.println("Error lexico en linea " + (yyline+1) + ", columna " + (yycolumn+1) + ": " + message + " Caracter: '" + yytext() + "'");
  }
%}


Letter = [a-zA-Z]
Digit = [0-9]
Identifier = ({Letter}|_)({Letter}|{Digit}|_)*
Integer = {Digit}+
NegInteger = "-" {Digit}+
Float = {Digit}+\.{Digit}+
NEGFloat = "-" {Digit}+\.{Digit}+
WhiteSpace = [ \t\r\n]+
CommentSingle = "|" [^\r\n]*
CommentMulti = "є" [^э]* "э"

%%

<YYINITIAL> {
  // Palabras Reservadas
  "world"       { return symbol(sym.WORLD); }
  "local"       { return symbol(sym.LOCAL); }
  "gift"        { return symbol(sym.GIFT); }
  "navidad"     { return symbol(sym.NAVIDAD); }
  "coal"        { return symbol(sym.COAL); }
  "decide"      { return symbol(sym.DECIDE); }
  "of"          { return symbol(sym.OF); }
  "end"         { return symbol(sym.END); }
  "loop"        { return symbol(sym.LOOP); }
  "exit"        { return symbol(sym.EXIT); }
  "when"        { return symbol(sym.WHEN); }
  "show"        { return symbol(sym.SHOW); }
  "get"         { return symbol(sym.GET); }
  "return"      { return symbol(sym.RETURN); }
  "break"       { return symbol(sym.BREAK); }
  "else"        { return symbol(sym.ELSE); }
  "endl"        { return symbol(sym.ENDL); }
  "for"         { return symbol(sym.FOR); }

  // Tipos
  "int"         { return symbol(sym.INT); }
  "float"       { return symbol(sym.FLOAT); }
  "boolean"     { return symbol(sym.BOOL); }
  "char"        { return symbol(sym.CHAR); }
  "string"      { return symbol(sym.STRING); }
  "true"        { return symbol(sym.LIT_BOOL, true); }
  "false"       { return symbol(sym.LIT_BOOL, false); }

  // Signos especiales
  "¡"           { return symbol(sym.BLOCK_START); }
  "!"           { return symbol(sym.BLOCK_END); }
  "¿"           { return symbol(sym.PAREN_OPEN); }
  "?"           { return symbol(sym.PAREN_CLOSE); }
  ","           { return symbol(sym.COMMA); }
  "->"          { return symbol(sym.ARROW); }
  ";"           { return symbol(sym.SEMICOLON); }
  "["           { return symbol(sym.LEFT_ARRAY); }
  "]"           { return symbol(sym.RIGHT_ARRAY); }

  // Operadores Aritméticos
  "+"           { return symbol(sym.PLUS); }
  "-"           { return symbol(sym.MINUS); }
  "*"           { return symbol(sym.MULT); }
  "//"          { return symbol(sym.DIV_INT); }
  "/"           { return symbol(sym.DIV); }
  "%"           { return symbol(sym.MOD); }
  "^"           { return symbol(sym.POWER); }
  "++"          { return symbol(sym.INC); }
  "--"          { return symbol(sym.DEC); }

  // Operadores Relacionales (AGREGADOS)
  "=="          { return symbol(sym.EQ_EQ); }
  "!="          { return symbol(sym.NOT_EQ); }
  "<="          { return symbol(sym.LESS_EQ); }
  ">="          { return symbol(sym.GREATER_EQ); }
  "="           { return symbol(sym.ASSIGN); }
  ">"           { return symbol(sym.GREATER); }
  "<"           { return symbol(sym.LESS); }

  // Lógicos
  "@"           { return symbol(sym.AND); }
  "~"           { return symbol(sym.OR); }
  "Σ"           { return symbol(sym.NOT); }

  // Literales
  {Integer}     { return symbol(sym.LIT_INT, Integer.parseInt(yytext())); }
  {NegInteger}  { return symbol(sym.LIT_NEG, Integer.parseInt(yytext())); }
  {Float}       { return symbol(sym.LIT_FLOAT, Double.valueOf(yytext())); }
  {NEGFloat}    { return symbol(sym.LIT_NEGFLOAT, Double.valueOf(yytext())); }
  {Identifier}  { return symbol(sym.IDENTIFIER, yytext()); }
  \"[^\"]*\"    { return symbol(sym.LIT_STRING, yytext()); }
  \'[^\']\'     { return symbol(sym.LIT_CHAR, yytext()); }

  {WhiteSpace}  { /* Ignorar */ }
  {CommentSingle} { /* Ignorar */ }
  {CommentMulti}  { /* Ignorar */ }

  .             {
                  error("Caracter ilegal detectado");
                  return symbol(sym.error, yytext());
                }
}