package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNCDEC extends AST_Node {
    public AST_TYPE type;
    public String name;
    public AST_ARG_LIST args;
    public AST_STMT_LIST stmt_lst;

    public String class_name;
	public String epilogue_label;

    public int parameters_num;
	public int local_var_num;

    public AST_FUNCDEC(AST_TYPE type, String name, AST_ARG_LIST args, AST_STMT_LIST stmt_lst, int line) {
        this.line = line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.args = args;
        this.name = name;
        this.stmt_lst = stmt_lst;
        this.type = type;

    }

    public void PrintMe() {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null)
            type.PrintMe();
        if (args != null)
            args.PrintMe();
        if (args != null)
            args.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNCDEC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (args != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, args.SerialNumber);
        if (args != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, args.SerialNumber);
        if (type != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
        int func_id = 0;
        boolean class_method, found_func = false;
        String current_scope = SYMBOL_TABLE.getInstance().current_scope;
        class_method = current_scope.equals("class");
        /* Function can only be declared in either global or class scope */
        if (!class_method && !current_scope.equals("global"))
            throw new SemanticException(line);
        /* Check that the arguments are valid */
        if (args != null) {
            TYPE_LIST func_args = (TYPE_LIST) args.SemantMe();
            while (func_args != null) {
                if (func_args.head.typeEnum == TypeEnum.TYPE_VOID
                        || !SYMBOL_TABLE.getInstance().isInstancable(func_args.head.name)) {
                    throw new SemanticException(line);
                }
                func_args = func_args.tail;
            }
        }
        if (!class_method) {
            /* Function can't be declared twice */
            if (SYMBOL_TABLE.getInstance().find(name) != null)
                throw new SemanticException(line);
        }
        /* Function is in class scope */
        else {
            /* Function can't be declared twice */
            if (SYMBOL_TABLE.getInstance().findInCurrentScope(name) != null)
                throw new SemanticException(line);

            func_id = SYMBOL_TABLE.getInstance().current_class.methods_counter;
            SYMBOL_TABLE.getInstance().current_class.methods_counter++;
            /*
             * We search in parents classes to check whether it's an override or an overload
             */
            TYPE_CLASS parent = SYMBOL_TABLE.getInstance().current_parent_class;
            while (parent != null && !found_func) {
                TYPE_LIST class_member = parent.data_members;
                while (class_member != null) {
                    /* Methods can't share names with data members */
                    if (class_member.dataMemberName.equals(name) && !(class_member.head.typeEnum == TypeEnum.TYPE_FUNCTION))
                        throw new SemanticException(line);
                    else if (!class_member.dataMemberName.equals(name) || !(class_member.head.typeEnum == TypeEnum.TYPE_FUNCTION)) {
                        class_member = class_member.tail;
                        continue;
                    }
                    TYPE_FUNCTION curr_func = (TYPE_FUNCTION) class_member.head;
                    TYPE_FUNCTION given_func = args == null
                            ? new TYPE_FUNCTION(type.SemantMe(), name, null,
                                    SYMBOL_TABLE.getInstance().current_class.name)
                            : new TYPE_FUNCTION(type.SemantMe(), name, (TYPE_LIST) args.SemantMe(),
                                    SYMBOL_TABLE.getInstance().current_class.name);
                    if (curr_func.equals(given_func)){
                        /* Override - Valid*/
                        SYMBOL_TABLE.getInstance().current_class.methods_counter--;
                        func_id = curr_func.func_index;
                        found_func = true;
                        break;
                    }
                    else{
                        /* Overload - Error */
                        throw new SemanticException(line);
                    }
                }
                parent = parent.father;
            }
        }
            /************************/
            /* Begin function scope */
            /************************/

            SYMBOL_TABLE.getInstance().beginScope("function");
            TYPE_CLASS current_class = SYMBOL_TABLE.getInstance().current_class;
            TYPE_FUNCTION func_type = null;
            if (args != null)
            {
                if (current_class != null) {
                    func_type = new TYPE_FUNCTION(type.SemantMe(), name, (TYPE_LIST) args.SemantMe(),SYMBOL_TABLE.getInstance().current_class.name);
                }
                else {
                    func_type = new TYPE_FUNCTION(type.SemantMe(), name, (TYPE_LIST) args.SemantMe(),null);

                }

                TYPE_LIST method_params = (TYPE_LIST) args.SemantMe();
                int arg_counter = 1;
                while (method_params != null)
                {
                    TYPE_VAR param_type = (TYPE_VAR) method_params.head;
                    param_type.var_kind = KindEnum.PARAM;
                    param_type.var_index = arg_counter;
                    param_type.var_label = null;
                    SYMBOL_TABLE.getInstance().enter(method_params.dataMemberName, param_type, false, SYMBOL_TABLE.getInstance().current_scope);
                    arg_counter++;
                    method_params = method_params.tail;
                }

                parameters_num = arg_counter - 1;
            }
            else
            {
                if (SYMBOL_TABLE.getInstance().current_class != null){
                    func_type = new TYPE_FUNCTION(type.SemantMe(), name, null,SYMBOL_TABLE.getInstance().current_class.name);
                }
                else {
                    func_type = new TYPE_FUNCTION(type.SemantMe(), name, null,null);
                }
                parameters_num = 0;
            }

            /***************************************************/
            /* [2] Enter the function Type to the Symbol Table */
            /***************************************************/
            if (SYMBOL_TABLE.getInstance().current_class != null){
                this.epilogue_label = "_"+name+"_"+SYMBOL_TABLE.getInstance().current_class.name+"_epilogue";
            }
            else
            {
                if (name.equals("main"))
                {
                    this.epilogue_label = "main_epilogue";
                }
                else
                {
                    this.epilogue_label = "_"+name+"_epilogue";
                }
            }
            func_type.epilogue_label = this.epilogue_label;
            SYMBOL_TABLE.getInstance().current_function = func_type;

            /* Check the validity of the statement types, using their SemantMe() function */
            stmt_lst.SemantMe();

            /*****************/
            /* [3] End Scope */
            /*****************/
            SYMBOL_TABLE.getInstance().endScope();
            SYMBOL_TABLE.getInstance().current_function = null;

            /***************************************************/
            /* [4] Enter the function Type to the Symbol Table */
            /***************************************************/

            /* Add the AST annotations */
            KindEnum func_kind = KindEnum.GLOBAL;
            int method_id = 0;
            String func_label = null;

            /* If it's a method */
            if (class_method)
            {
                this.class_name = SYMBOL_TABLE.getInstance().current_class.name;
                func_kind = KindEnum.METHOD;
                method_id = func_id;
            }
            /* Else, it's a function */
            else
            {
                this.class_name = null;
                func_label = "_"+name;
            }

            func_type.func_kind = func_kind;
            func_type.func_index = method_id;
            func_type.func_label = func_label;

            SYMBOL_TABLE.getInstance().enter(name, func_type, false, SYMBOL_TABLE.getInstance().current_scope);

            this.local_var_num = func_type.local_counter - 1;

            /* Return the TYPE_FUNCTION */
            return func_type;

        }

    }

