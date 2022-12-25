package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_VARDEC extends AST_Node {

    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP newExp;

    /* AST ANNOTATIONS */
    private KindEnum var_kind;
    private int var_index;
    private String var_label;


    public AST_VARDEC(AST_TYPE type, String name, AST_EXP exp, AST_NEW_EXP newExp){

        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.type= type;
        this.exp = exp;
        this.name = name;
        this.newExp = newExp;
    }

    public void PrintMe(){
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();
        if (newExp != null) newExp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ASSIGN\nVARDEC(%s)",name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
        if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
    }
    public TYPE SemantMe() throws SemanticException
    {
        boolean is_instancable;
        boolean is_equals_class_name = false;
        TYPE newExp_type = null;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        /* Check if the var type is same as the class type which we are in (If we are in class scope) */
        if (SYMBOL_TABLE.getInstance().current_class != null && type.typeName != null)
        {
            is_equals_class_name = SYMBOL_TABLE.getInstance().current_class.name.equals(type.typeName);
        }

        is_instancable = SYMBOL_TABLE.getInstance().isInstancable(type.typeName);

        /* Check if the variable type is a valid type (Declared and instance-able type) */
        if (!is_equals_class_name && !is_instancable)
        {
            throw new SemanticException(line);
        }

        TYPE type_semant = type.SemantMe();

        /* Make sure that the variable type is NOT void */
        if (type_semant.typeEnum == TypeEnum.TYPE_VOID)
        {
            throw new SemanticException(line);
        }

        /* If there is an expression, make sure that its type matches the var type */
        TYPE exp_type = null;
        if (exp != null)
        {
            exp_type = exp.SemantMe();
            if (exp_type.typeEnum != type_semant.typeEnum)
            {
                if (exp_type.typeEnum == TypeEnum.TYPE_NIL)
                {
                    if (type_semant.typeEnum != TypeEnum.TYPE_CLASS && type_semant.typeEnum != TypeEnum.TYPE_ARRAY)
                    {
                        throw new SemanticException(line);
                    }
                }
                else
                {
                    throw new SemanticException(line);
                }
            }

            else if (exp_type.typeEnum == TypeEnum.TYPE_CLASS)
            {
                TYPE_CLASS exp_class = (TYPE_CLASS) exp_type;
                TYPE_CLASS var_class = (TYPE_CLASS) type_semant;
                if (!var_class.is_replacable(exp_class)) throw new SemanticException(line);
            }

            else if (exp_type.typeEnum == TypeEnum.TYPE_ARRAY)
            {
                TYPE_ARRAY exp_array = (TYPE_ARRAY) exp_type;
                TYPE_ARRAY var_array = (TYPE_ARRAY) type_semant;
                if (!var_array.is_replacable(exp_array)) throw new SemanticException(line);
            }
        }

        else if (newExp != null)
        {
            newExp_type = newExp.SemantMe();
            if (newExp_type.typeEnum != type_semant.typeEnum)
            {
                throw new SemanticException(line);
            }
            else if (newExp_type.typeEnum == TypeEnum.TYPE_CLASS)
            {
                TYPE_CLASS newexp_class = (TYPE_CLASS) newExp_type;
                TYPE_CLASS var_class = (TYPE_CLASS) type_semant;
                if (!var_class.is_replacable(newexp_class)) throw new SemanticException(line);
            }

            else if (newExp_type.typeEnum == TypeEnum.TYPE_ARRAY)
            {
                TYPE_ARRAY newexp_array = (TYPE_ARRAY) newExp_type;
                TYPE_ARRAY var_array = (TYPE_ARRAY) type_semant;
                if (!var_array.is_replacable_new(newexp_array))
                {
                    throw new SemanticException(line);
                }
            }
        }

        /**************************************/
        /* [2] Check That Name does NOT exist */
        /**************************************/
        TYPE found_duplicate = SYMBOL_TABLE.getInstance().findInCurrentScope(name);
        if (found_duplicate != null)
        {
            if (!SYMBOL_TABLE.getInstance().isInstancable(name))
            {
                throw new SemanticException(line);
            }
        }

        /***************************************************/
        /* [3] Enter the variable Type to the Symbol Table */
        /***************************************************/
        KindEnum var_kind = KindEnum.GLOBAL;
        int var_index = 0;
        String var_label = null;

        /* If we are in a class scope (and not in a method scope) */
        if (SYMBOL_TABLE.getInstance().current_class != null && SYMBOL_TABLE.getInstance().current_function == null)
        {
            var_kind = KindEnum.FIELD;
            var_index = SYMBOL_TABLE.getInstance().current_class.fields_counter;
            SYMBOL_TABLE.getInstance().current_class.fields_counter++;
        }
        /* Else, if we are in a function / method scope */
        else if (SYMBOL_TABLE.getInstance().current_function != null)
        {
            var_kind = KindEnum.LOCAL;
            var_index = SYMBOL_TABLE.getInstance().current_function.local_counter;
            SYMBOL_TABLE.getInstance().current_function.local_counter++;
        }

        else
        {
            var_label = "_"+name;
        }

        TYPE_VAR var_type = new TYPE_VAR(type_semant, name);

        var_type.var_kind = var_kind;
        var_type.var_index = var_index;
        var_type.var_label = var_label;

        /* Add the AST annotations */
        this.var_kind = var_kind;
        this.var_index = var_index;
        this.var_label = var_label;
        //soso
        /* If it's a class field declaration with default value assigment */
        if (exp != null && SYMBOL_TABLE.getInstance().current_class != null && SYMBOL_TABLE.getInstance().current_function == null)
        {
            /* If it's an int assigment, it has to constant int */
            if (exp_type.typeEnum == TypeEnum.TYPE_INT)
            {
                AST_EXP_INT constant_int = (AST_EXP_INT) exp;
                var_type.default_int_value = constant_int.value;
            }

            /* If it's a string assigment, it has to be constant string */
            else if (exp_type.typeEnum == TypeEnum.TYPE_STRING)
            {
                AST_EXP_STRING constant_string = (AST_EXP_STRING) exp;
                //--------------------- TODO - WAS string_label -------------------------------------------
                var_type.default_string_label = constant_string.str;
            }

            else if (exp_type.typeEnum == TypeEnum.TYPE_NIL)
            {
                var_type.default_int_value = 0;
            }
        }

        SYMBOL_TABLE.getInstance().enter(name, var_type, false, SYMBOL_TABLE.getInstance().current_scope);

        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return var_type;
    }
}
