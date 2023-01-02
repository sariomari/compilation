package AST;
import TYPES.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public AST_NEW_EXP newExp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, AST_NEW_EXP newExp, int line)
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
		this.exp = exp;
		this.newExp = newExp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();
		if (newExp != null) newExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nASSIGN");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}
	public TYPE SemantMe() throws SemanticException
	{
		if (exp != null){
			TYPE t1 = null;
			if (var != null) t1 = var.SemantMe();

			TYPE t2 = null;
			if (exp != null) t2 = exp.SemantMe();

			if (t1 == null)
			{
				throw new SemanticException(line);
			}
			else if (t1.isClass() && t2.isClass())
			{
				if (!(((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1)) && ((TYPE_CLASS)t1).name != ((TYPE_CLASS)t2).name)
				{
					throw new SemanticException(line);
				}
			}
			else if (t1.isArray() && t2.isArray())
			{
				if (((TYPE_ARRAY)t1).name != ((TYPE_ARRAY)t2).name)
				{
					throw new SemanticException(line);
				}
			}
			else if (t1.isArray() || t1.isClass())
			{
				if (t2 != TYPE_NIL.getInstance())
				{
					throw new SemanticException(line);
				}
			}
			else if (t1 != t2)
			{
				throw new SemanticException(line);
			}
			return null;

		} else { // newExp != null
			TYPE t1 = null;
			if (var != null) t1 = var.SemantMe();

			TYPE t2 = null;
			if (newExp != null) t2 = newExp.SemantMe();

			if (t1 == null || t2 == null)
			{
				throw new SemanticException(line);
			}
			else if (t1.isClass() && t2.isClass())
			{
				if (!(((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1)))
				{
					throw new SemanticException(line);
				}
			}
			else if (t1.isArray())
			{
				if (!(((TYPE_ARRAY)t1).type == t2))
				{
					throw new SemanticException(line);
				}
			}
			else if (t1 != t2)
				throw new SemanticException(line);

			return null;
		}
	}
}
