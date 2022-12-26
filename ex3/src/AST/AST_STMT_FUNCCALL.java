package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_FUNCCALL extends AST_STMT
{
    public String fname;
    public AST_VAR v;
    public AST_EXP_LIST l;
    public int method_id;

    /******/
    /* CONSTRUCTOR(S) */
    /******/
    public AST_STMT_FUNCCALL(String fname, AST_VAR v, AST_EXP_LIST l, int line)
    {
        this.line = line;
        /**********/
        /* SET A UNIQUE SERIAL NUMBER */
        /**********/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /*************/
        System.out.print("====================== exp -> [obj.]f(expLst);\n");

        /***********/
        /* COPY INPUT DATA NENBERS ... */
        /***********/
        this.fname = fname;
        this.v = v;
        this.l = l;
    }

    /*****************/
    /* The printing message for a binop exp AST node */
    /*****************/
    public void PrintMe()
    {

        /***********/
        /* CONVERT OP to a printable sOP */
        /***********/

        /*************/
        /* AST NODE TYPE = AST BINOP EXP */
        /*************/
        System.out.print("AST NODE FUNC CALL STMT\n");

        /**************/
        /* RECURSIVELY PRINT left + right ... */
        /**************/
        if (fname != null) System.out.format("function %s", fname);
        if (v != null) v.PrintMe();
        if (l != null) l.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNCCALL(%s)",fname));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (l  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,l.SerialNumber);
        if (v  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        TYPE_LIST found_arg_list;
        TYPE_FUNCTION found_method = null;
        TYPE found_function = null;
        boolean is_found = false;

        /* If it's a method */
        if (v != null)
        {
            if (l == null)
            {
                if (TYPE_FUNCTION.is_recursive_call(fname, null) == true)
                {
                    return SYMBOL_TABLE.getInstance().current_function.returnType;
                }
            }
            else
            {
                if (TYPE_FUNCTION.is_recursive_call(fname, (TYPE_LIST) l.SemantMe()) == true)
                {
                    return SYMBOL_TABLE.getInstance().current_function.returnType;
                }
            }
            TYPE var_type = v.SemantMe();

            /* Make sure that var is a class */
            if (var_type.typeEnum != TypeEnum.TYPE_CLASS)
            {
                throw new SemanticException(line);
            }

            /* Find the method of the given class */
            TYPE_CLASS curr_class = (TYPE_CLASS) ((TYPE_VAR) var_type).var_type;

            while(curr_class != null && !is_found)
            {
                TYPE_LIST curr_data_member = curr_class.data_members;
                while(curr_data_member != null)
                {
                    if (curr_data_member.dataMemberName.equals(fname))
                    {
                        if (curr_data_member.head.typeEnum == TypeEnum.TYPE_FUNCTION)
                        {
                            found_method = (TYPE_FUNCTION) curr_data_member.head;
                            is_found = true;
                            break;
                        }
                        throw new SemanticException(line);
                    }

                    curr_data_member = curr_data_member.tail;
                }

                curr_class = curr_class.father;
            }

            if (!is_found)
            {
                throw new SemanticException(line);
            }

            found_arg_list = found_method.params;
            this.method_id = found_method.func_index;
        }

        /* Else, it's a function */
        else
        {
            if (l == null)
            {
                if (TYPE_FUNCTION.is_recursive_call(fname, null) == true)
                {
                    return SYMBOL_TABLE.getInstance().current_function.returnType;
                }
            }
            else
            {
                if (TYPE_FUNCTION.is_recursive_call(fname, (TYPE_LIST) l.SemantMe()) == true)
                {
                    return SYMBOL_TABLE.getInstance().current_function.returnType;
                }
            }
            found_function = SYMBOL_TABLE.getInstance().find(fname);

            /* Make sure that the found ID in the symbol table is a function */
            if (found_function.typeEnum != TypeEnum.TYPE_FUNCTION)
            {
                throw new SemanticException(line);
            }

            found_arg_list = ((TYPE_FUNCTION) found_function).params;
        }

        /* Compare the given function arguments with the found function arguments */
        TYPE_LIST given_arg_list;
        if (l != null)
        {
            given_arg_list = (TYPE_LIST) l.SemantMe();
        }
        else
        {
            given_arg_list = null;
        }
        while(given_arg_list != null && found_arg_list != null)
        {
            if (given_arg_list.head.typeEnum != found_arg_list.head.typeEnum)
            {
                if (!(given_arg_list.head.typeEnum == TypeEnum.TYPE_NIL && (found_arg_list.head.typeEnum == TypeEnum.TYPE_CLASS || found_arg_list.head.typeEnum == TypeEnum.TYPE_ARRAY)))
                {
                    throw new SemanticException(line);
                }
            }

            if (given_arg_list.head.typeEnum == TypeEnum.TYPE_CLASS)
            {
                TYPE_CLASS given_class = (TYPE_CLASS) given_arg_list.head;
                TYPE_CLASS found_class = (TYPE_CLASS) ((TYPE_VAR) found_arg_list.head).var_type;
                if (!found_class.is_replacable(given_class)) throw new SemanticException(line);
            }

            else if (given_arg_list.head.typeEnum == TypeEnum.TYPE_ARRAY)
            {
                TYPE_ARRAY given_array = (TYPE_ARRAY) given_arg_list.head;
                TYPE_ARRAY found_array = (TYPE_ARRAY) ((TYPE_VAR) found_arg_list.head).var_type;
                if (!found_array.is_replacable(given_array)) throw new SemanticException(line);
            }

            given_arg_list = given_arg_list.tail;
            found_arg_list = found_arg_list.tail;
        }

        if (given_arg_list != null || found_arg_list != null)
        {
            throw new SemanticException(line);
        }

        return null;
    }
}
