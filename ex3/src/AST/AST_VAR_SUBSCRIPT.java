package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int line)
	{
		this.line = ++line;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR\nSUBSCRIPT");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		TYPE t1 = null;
		TYPE t2 = null;
		if (var != null) t1 = var.SemantMe();
		if (subscript != null) t2 = subscript.SemantMe();

		if(t1 == null){
			throw new SemanticException(line);
		}

		if(!t1.isArray() || t2 != TYPE_INT.getInstance()){
			throw new SemanticException(line);
		}

		else if(subscript instanceof AST_EXP_INT){
			if (((AST_EXP_INT)subscript).value < 0){
				throw new SemanticException(line);
			}
		}
		return ((TYPE_ARRAY)t1).type;
	}
}
