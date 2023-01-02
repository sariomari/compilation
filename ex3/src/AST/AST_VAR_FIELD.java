package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

// not complete
public class AST_VAR_FIELD extends AST_VAR
{
	// var.varField
	// call to fields of object without methods
	public AST_VAR var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		this.line = ++line;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\nFIELD(%s)",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe() throws SemanticException {
		TYPE t1 = null;
		TYPE t2 = null;

		t1 = var.SemantMe();
		//t2 = SYMBOL_TABLE.getInstance().find(fieldName);


		if(t1 == null){
			throw new SemanticException(line);
		}
		if(!t1.isClass()){
			throw new SemanticException(line);
		}

		if ((TYPE_CLASS)t1 == SYMBOL_TABLE.getInstance().curr_class){
			int i;
			for(i = 0; i < SYMBOL_TABLE.getInstance().index; i++){
				System.out.format("%s\n",SYMBOL_TABLE.getInstance().arr[i].name);
				System.out.format("%s\n",fieldName);
				System.out.format("%s\n",SYMBOL_TABLE.getInstance().arr[i].isMethod);
				if(SYMBOL_TABLE.getInstance().arr[i].name.equals(fieldName) && SYMBOL_TABLE.getInstance().arr[i].isMethod == false){
					return SYMBOL_TABLE.getInstance().arr[i].t;
				}
			}
		}

		t2 = ((TYPE_CLASS)t1).findMembers(fieldName, false);
		if(t2 == null){ //if fieldName is not a data member of our class
			throw new SemanticException(line);
		}
		return t2;
	}
}
