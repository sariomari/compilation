package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name)
	{
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
		/* Make sure that the variable is already declared */
		TYPE found_type = SYMBOL_TABLE.getInstance().find(name);
		if (found_type != null)
		{
			/* Make sure that the variable is NOT a function */
			if (found_type.typeEnum != TypeEnum.TYPE_FUNCTION)
			{
				TYPE_VAR var_type = (TYPE_VAR) found_type;
				/* Add the AST annotations */
				this.var_kind = var_type.var_kind;
				this.var_index = var_type.var_index;
				this.var_label = var_type.var_label;

				return var_type;
			}
		}
		throw new SemanticException(line);
	}
}
