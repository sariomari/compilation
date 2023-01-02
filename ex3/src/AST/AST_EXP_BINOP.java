package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP, int line)
	{
		this.line = ++line;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";

		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}

		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("BINOP(%s)",sOP));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		TYPE t1 = null;
		TYPE t2 = null;

		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();
//		System.out.println("Comparing t1: " + t1 + " left: " + left + " t2: " + t2 + " right: " + right);

		System.out.format("%s\n", t1.name);
		System.out.format("%s\n", t1.name);
		System.out.format("%s\n", t1.isArray());
		System.out.format("%s\n", t2.isArray());

		if(t1 == null || t2 == null){
			throw new SemanticException(line);
		}
		// int x; int y; x OP y
		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance()) )
		{
			if (OP == 3 && right instanceof AST_EXP_INT && ((AST_EXP_INT) right).value == 0){
				System.out.format("divide by zero\n");
				throw new SemanticException(line);
			}
			return TYPE_INT.getInstance();
		}
		// string x; string y; x OP y && op == "="
		else if(t1 == TYPE_STRING.getInstance() && t2 == TYPE_STRING.getInstance() && OP == 0){
			return TYPE_STRING.getInstance();
		}
		else if(t1 == TYPE_STRING.getInstance() && t2 == TYPE_STRING.getInstance() && OP == 6){
			return TYPE_INT.getInstance();
		}
		else if(((t1.isClass() && t2.isClass() && (t1 == t2 || ((TYPE_CLASS)t1).isAncestor((TYPE_CLASS)t2) || ((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1))) ||
				(t1.isClass() && t2 == TYPE_NIL.getInstance()) ||
				(t2.isClass() && t1 == TYPE_NIL.getInstance())) &&
				OP == 6){
			return TYPE_INT.getInstance();
		}
		else if(t1.isArray() && t2.isArray() && ((TYPE_ARRAY)t1).name == ((TYPE_ARRAY)t2).name && OP == 6){
			return TYPE_INT.getInstance();
		}
		else if((t1 == TYPE_VOID.getInstance()) && (t2 == TYPE_VOID.getInstance()) && OP ==6){
			return TYPE_INT.getInstance();
		}
		throw new SemanticException(line);
	}
}
