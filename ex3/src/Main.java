
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l=null;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
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

			try
			{
				/******************************/
				/* [3] Initialize a new lexer */
				/******************************/

				l = new Lexer(file_reader);

				/*******************************/
				/* [4] Initialize a new parser */
				/*******************************/
				p = new Parser(l);

				/***********************************/
				/* [5] 3 ... 2 ... 1 ... Parse !!! */
				/***********************************/
				AST = (AST_PROGRAM) p.parse().value;

				/*************************/
				/* [6] Print the AST ... */
				/*************************/
				AST.PrintMe();

				/**************************/
				/* [7] Semant the AST ... */
				/**************************/
				AST.SemantMe();

				/**************************/
				/* [8] Write 'OK' ...     */
				/**************************/
				file_writer.print("OK");

				/*************************************/
				/* [9] Finalize AST GRAPHIZ DOT file */
				/*************************************/
				AST_GRAPHVIZ.getInstance().finalizeFile();
			}

			catch (SemanticException e)
			{
				file_writer.print("ERROR(");
				file_writer.print(e.line);
				System.out.format("ERROR_IN_LINE(%s)", e.line);
				file_writer.print(")");
				e.printStackTrace();
			}
			catch (Exception e)
			{
				file_writer.print("ERROR(");
				file_writer.print(l.getLine());
				file_writer.print(")");
				e.printStackTrace();
			}
			finally {
				file_writer.close();
			}

		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}


	}
}


