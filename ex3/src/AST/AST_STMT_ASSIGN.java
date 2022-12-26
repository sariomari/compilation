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
		this.line = line;
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
		if(this.exp == null && this.newExp==null){
			throw new SemanticException(line);
		}
		/* ASSIGNING AN EXPRESSION */
		if (this.exp != null && this.newExp==null){
			TYPE var_type = var.SemantMe();
			TYPE exp_type = exp.SemantMe();

			/* Make sure that the types of both sides of the assign are equal */

			/* If they're both classes, compare their names */
			if (var_type.typeEnum == TypeEnum.TYPE_CLASS && exp_type.typeEnum == TypeEnum.TYPE_CLASS)
			{
				TYPE_CLASS exp_class = (TYPE_CLASS) exp_type;
				TYPE_CLASS var_class;
				if (var_type instanceof TYPE_VAR)
				{
					var_class = (TYPE_CLASS) ((TYPE_VAR) var_type).var_type;
				}
				else
				{
					var_class = (TYPE_CLASS) var_type;
				}
				if (!var_class.is_replacable(exp_class)) throw new SemanticException(line);
			}
			/* If they're both arrays, compare their names */
			else if (var_type.typeEnum == TypeEnum.TYPE_ARRAY && exp_type.typeEnum == TypeEnum.TYPE_ARRAY)
			{
				TYPE_ARRAY exp_array = (TYPE_ARRAY) exp_type;
				TYPE_ARRAY var_array;
				if (var_type instanceof TYPE_VAR)
				{
					var_array = (TYPE_ARRAY) ((TYPE_VAR) var_type).var_type;
				}
				else
				{
					var_array = (TYPE_ARRAY) var_type;
				}
				if (!var_array.is_replacable(exp_array)) throw new SemanticException(line);
			}

			/* Else they're not classes or arrays, then make sure that their types are equal */
			else if (var_type.typeEnum == TypeEnum.TYPE_FUNCTION || exp_type.typeEnum == TypeEnum.TYPE_FUNCTION)
			{
				throw new SemanticException(line);
			}
			else if(var_type.typeEnum != exp_type.typeEnum)
			{
				if (exp_type.typeEnum == TypeEnum.TYPE_NIL)
				{
					if (var_type.typeEnum != TypeEnum.TYPE_CLASS && var_type.typeEnum != TypeEnum.TYPE_ARRAY)
					{
						throw new SemanticException(line);
					}
				}
				else
				{
					throw new SemanticException(line);
				}
			}
		}

		/*ASSIGNING A NEW EXP */
		else if(this.exp == null && this.newExp!=null){
			TYPE var_type = var.SemantMe();
			TYPE new_exp_type = newExp.SemantMe();

			/* Make sure that the types of both sides of the assign are equal */

			/* If they're both classes, compare them */
			if (var_type.typeEnum == TypeEnum.TYPE_CLASS && new_exp_type.typeEnum == TypeEnum.TYPE_CLASS)
			{
				TYPE_CLASS new_exp_class = (TYPE_CLASS) new_exp_type;
				TYPE_CLASS var_class;

				if (var_type instanceof TYPE_VAR) {
					var_class = (TYPE_CLASS) ((TYPE_VAR) var_type).var_type;
				}
				else {
					var_class = (TYPE_CLASS) var_type;
				}
				if (!var_class.is_replacable(new_exp_class)) throw new SemanticException(line);
			}

			/* If they're both arrays, compare them */
			else if (var_type.typeEnum == TypeEnum.TYPE_ARRAY && new_exp_type.typeEnum == TypeEnum.TYPE_ARRAY)
			{
				TYPE_ARRAY var_array;
				if (var_type instanceof TYPE_VAR){
					var_array = (TYPE_ARRAY) ((TYPE_VAR) var_type).var_type;
				}
				else {
					var_array = (TYPE_ARRAY) var_type;
				}
				TYPE_ARRAY newexp_array = (TYPE_ARRAY) new_exp_type;
				/* If their arrayMembersType NOT equal, return error */
				if (!var_array.is_replacable_new(newexp_array)) throw new SemanticException(line);
			}
			/* Else, it's an error */
			else
			{
				throw new SemanticException(line);
			}
		}
		return null;
	}
}
