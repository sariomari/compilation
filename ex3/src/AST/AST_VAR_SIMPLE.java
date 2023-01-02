package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	// int x = y;
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int line)
	{
		this.line = ++line;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;

	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\nSIMPLE\n(%s)",name));
	}

	public TYPE SemantMe() throws SemanticException
	{
		TYPE t1 = null;

		t1 = SYMBOL_TABLE.getInstance().findInScope(name, 1);
		if(t1 != null){
			return t1;
		}
		TYPE_CLASS cur_class = SYMBOL_TABLE.getInstance().curr_class;
		if (cur_class != null){
			if (cur_class.father != null){
				t1 = TYPE_CLASS.findMembers_test(cur_class.father, name, false);
				if (t1 != null){
					return t1;
				}
			}

		}
		t1 = SYMBOL_TABLE.getInstance().find(name, 1);

		if(t1 == null){
			throw new SemanticException(line);
		}

//		if(t1.isArray()){
//			System.out.println("Array");
//		}
//		if(t1.isClass()){
//			System.out.println("Class");
//		}
		return t1;
	}
}
