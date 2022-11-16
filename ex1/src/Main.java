   
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/

				String var = getSymbol(s);
				System.out.print(var);
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]");
				System.out.print("\n");

				
				/*********************/
				/* [7] Print to file */
				/*********************/
				file_writer.print(var);
				file_writer.print("[");
				file_writer.print(l.getLine());
				file_writer.print(",");
				file_writer.print(l.getTokenStartPosition());
				file_writer.print("]");
				file_writer.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private static String getSymbol(Symbol s){
		switch (s.sym) {
			case 0:
				break;
			case 1:
				return "PLUS";
			case 2:
				return("MINUS");
			case 3:
				return("TIMES");
			case 4:
				return("DIVIDE");
			case 5:
				StringBuilder sb = new StringBuilder("INT");
				sb.append("(");
				sb.append(s.value.toString());
				sb.append(")");
				return sb.toString();
			case 6:
				StringBuilder sb1 = new StringBuilder("STRING");
				sb1.append("(");
				sb1.append(s.value.toString());
				sb1.append(")");
				return sb1.toString();
			case 7:
				StringBuilder sb2 = new StringBuilder("ID");
				sb2.append("(");
				sb2.append(s.value.toString());
				sb2.append(")");
				return sb2.toString();
			case 8:
				return("LBRACK");
			case 9:
				return("RBRACK");
			case 10:
				return("LBRACE");
			case 11:
				return("RBRACE");
			case 12:
				return("LPAREN");
			case 13:
				return("RPAREN");
			case 14:
				return("NIL");
			case 15:
				return("COMMA");
			case 16:
				return("DOT");
			case 17:
				return("SEMICOLON");
			case 18:
				return("ASSIGN");
			case 19:
				return("EQ");
			case 20:
				return("LT");
			case 21:
				return("GT");
			case 22:
				return("TYPE_INT");
			case 23:
				return("TYPE_VOID");
			case 24:
				return("TYPE_STRING");
			case 25:
				return("ARRAY");
			case 26:
				return("CLASS");
			case 27:
				return("EXTENDS");
			case 28:
				return("RETURN");
			case 29:
				return("WHILE");
			case 30:
				return("IF");
			case 31:
				return("NEW");
			case 32:
				return("ERROR");
		}
		return "";
	}

}


