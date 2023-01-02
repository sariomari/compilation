package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_VAR extends AST_EXP
{
	public AST_VAR var;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR(AST_VAR var, int line)
	{
		this.line = ++line;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA  ... */
		/*******************************/
		this.var = var;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null) var.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
			
	}

	public TYPE SemantMe() throws SemanticException {
		if (var != null) return var.SemantMe();
//		throw new SemanticException(line);
		return null;
	}
}
