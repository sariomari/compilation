package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body, int line)
	{
		this.line = line;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
	}

	/***********************************************************/
	/* The printing message for an function statement AST node */
	/***********************************************************/
	public void PrintMe()
	{
		/***************************************/
		/* RECURSIVELY PRINT VAR + EXPLIST ... */
		/***************************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"STMT\nIF");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException
	{
		/* Make sure that the if's condition is an int */
		if (cond.SemantMe().typeEnum != TypeEnum.TYPE_INT)
		{
			throw new SemanticException(line);
		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope("if");

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantMe();

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}
}