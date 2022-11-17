/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 

	/**********************************************/
	/* Enable token position extraction from main */  
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/

LineTerminator = \r|\n|\r\n
NonLineTerminator = [ \t\f]
WhiteSpace = {LineTerminator} | {NonLineTerminator}

COMMENT_STAR = ([A-Za-z0-9]|{WhiteSpace}|[(){}\[\]?!*+-\.;])
COMMENT_SLASH = ([A-Za-z0-9]|{WhiteSpace}|[(){}\[\]?!/+-\.;])
COMMENT_PADDING = ([A-Za-z0-9]|{WhiteSpace}|[(){}\[\]?!+-\.;])
COMMENT_CONTENT = ({COMMENT_STAR}*{COMMENT_PADDING}{COMMENT_SLASH}*)* | {COMMENT_STAR}* | {COMMENT_SLASH}*
TYPE1_COMMENT = ("//"([A-Za-z0-9]|{NonLineTerminator}|[(){}\[\]?!*+-/\.;])*{LineTerminator})
TYPE2_COMMENT = ("/*"{COMMENT_CONTENT}"*/")

INTEGER = 0 | [1-9][0-9]*
STRING = \"[a-zA-Z]*\"
ID = [a-zA-Z][a-zA-Z0-9]*

INVALID_ID = [^ID]
INVALID_COMMENT = (\/\*)|(\/\/{LineTerminator})
INVALID_INTEGER = (0+[0-9]+) | ([0-9][0-9][0-9][0-9][0-9][0-9]+)






/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
{TYPE1_COMMENT}			{}
{TYPE2_COMMENT}			{}
{INVALID_INTEGER}		{return symbol(TokenNames.ERROR);}
{INVALID_COMMENT}		{return symbol(TokenNames.ERROR);}


"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"				        { return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}

"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["					{return symbol(TokenNames.LBRACK);}
"]"					{return symbol(TokenNames.RBRACK);}
"{"					{return symbol(TokenNames.LBRACE);}
"}"					{return symbol(TokenNames.RBRACE);}

","					{return symbol(TokenNames.COMMA);}
"."					{return symbol(TokenNames.DOT);}
";"					{return symbol(TokenNames.SEMICOLON);}

":="					{return symbol(TokenNames.ASSIGN);}
"="					{return symbol(TokenNames.EQ);}
"<"					{return symbol(TokenNames.LT);}
">"					{return symbol(TokenNames.GT);}

"nil"                                   {return symbol(TokenNames.NIL);}
"int"                                   {return symbol(TokenNames.TYPE_INT);}
"void"                                  {return symbol(TokenNames.TYPE_VOID);}
"string"                                {return symbol(TokenNames.TYPE_STRING);}

"array"                                 {return symbol(TokenNames.ARRAY);}
"extends"                               {return symbol(TokenNames.EXTENDS);}
"return"                                {return symbol(TokenNames.RETURN);}
"while"                                 {return symbol(TokenNames.WHILE);}
"if"                                    {return symbol(TokenNames.IF);}
"new"                                   {return symbol(TokenNames.NEW);}
"class"					{return symbol(TokenNames.CLASS);}





{INTEGER}			{ int x = new Integer(yytext());
				  if(x<0 || x > 32767){ return symbol(TokenNames.ERROR);}
				  return symbol(TokenNames.INTEGER, x);}
{STRING}			{ return symbol(TokenNames.STRING, new String(yytext()));}
{ID}				{ return symbol(TokenNames.ID,     new String(yytext()));}   
{WhiteSpace}		        { /* just skip what was found, do nothing */ }
{INVALID_ID}			{return symbol(TokenNames.ERROR);}

<<EOF>>				{ return symbol(TokenNames.EOF);}

}
