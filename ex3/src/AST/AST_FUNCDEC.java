package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNCDEC extends AST_Node {
    /*****************************/
    /*  TYPE ID ([TYPE ID]*) {stmts} */
    /*****************************/
    public AST_TYPE returnTypeName;
    public String func;
    public AST_ARG_LIST params;
    public AST_STMT_LIST stmt_lst;

    public String class_name;
	public String epilogue_label;

    public int parameters_num;
	public int local_var_num;

    public AST_FUNCDEC(AST_TYPE returnTypeName, String func, AST_ARG_LIST params, AST_STMT_LIST stmt_lst, int line) {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.params = params;
        this.func = func;
        this.stmt_lst = stmt_lst;
        this.returnTypeName = returnTypeName;

    }

    public void PrintMe() {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (returnTypeName != null)
            returnTypeName.PrintMe();
        if (params != null)
            params.PrintMe();
        if (stmt_lst != null)
            stmt_lst.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNCDEC(%s)", func));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/

        if (returnTypeName != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, returnTypeName.SerialNumber);
        if (params != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, params.SerialNumber);
        if (stmt_lst != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, stmt_lst.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        TYPE t;
        TYPE returnType = null;
        TYPE_LIST reversed_list = null;
        TYPE_LIST type_list = null;

        if(func != null){
            if(SYMBOL_TABLE.getInstance().findInScope(func, 1) != null) {
                System.out.format("%s funcName was declared before in this scope", func);
                throw new SemanticException(line);
            }
        }


        /*******************/
        /* [0] return type */
        /*******************/
        returnType = returnTypeName.SemantMe();
        if (returnType == null){
            System.out.format("returnType was not fount");
            throw new SemanticException(line);
        }
        if(SYMBOL_TABLE.getInstance().func_scope){
            System.out.format("declare a new func in a scope func");
            throw new SemanticException(line);
        }

        SYMBOL_TABLE.getInstance().funcRetType = returnType;
        SYMBOL_TABLE.getInstance().func_scope = true;



        /***************************************************/
        /* [5] Enter the Function Type to the Symbol Table */
        /***************************************************/
        AST_ARG_LIST it = params;
        while(it != null)
        {
            t = it.head.type.SemantMe();
            System.out.println("In dec func param name:  "+ it.head.name);
            if (t == null )
            {
                System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type);
                throw new SemanticException(line);

            }else if( !(t == TYPE_INT.getInstance() || t == TYPE_STRING.getInstance() ||
                    (t.isClass() && SYMBOL_TABLE.getInstance().find(((TYPE_CLASS)t).name, 0) != null)  ||
                    (t.isArray() && SYMBOL_TABLE.getInstance().find(((TYPE_ARRAY)t).name, 0) != null))){
                System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type);
                throw new SemanticException(line);

            }
            else
            {
                reversed_list = new TYPE_LIST(t, reversed_list);
//                SYMBOL_TABLE.getInstance().enter(it.head.name, t, 1);
            }
            it = it.tail;
        }

        for (TYPE_LIST rt = reversed_list; rt != null; rt = rt.tail)
        {
            type_list = new TYPE_LIST(rt.head, type_list);
        }
        TYPE func_type = new TYPE_FUNCTION(returnType, func, type_list);
        SYMBOL_TABLE.getInstance().enter(func, func_type, 1);


        /****************************/
        /* [1] Begin Function Scope */
        /****************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Input Params */
        /***************************/
        System.out.println("------------------");
        it = params;
        while(it != null)
        {
            t = it.head.type.SemantMe();
            System.out.println("In dec func param name:  "+ it.head.name);
            if (t == null )
            {
                System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type);
                throw new SemanticException(line);

            }else if( !(t == TYPE_INT.getInstance() || t == TYPE_STRING.getInstance() ||
                    (t.isClass() && SYMBOL_TABLE.getInstance().find(((TYPE_CLASS)t).name, 0) != null)  ||
                    (t.isArray() && SYMBOL_TABLE.getInstance().find(((TYPE_ARRAY)t).name, 0) != null))){
                System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type);
                throw new SemanticException(line);
            }
            else
            {
                reversed_list = new TYPE_LIST(t, reversed_list);
                SYMBOL_TABLE.getInstance().enter(it.head.name, t, 1);
            }
            it = it.tail;
        }

        /*******************/
        /* [3] Semant Body */
        /*******************/
        stmt_lst.SemantMe();

        /*****************/
        /* [4] End Scope */
        /*****************/


//        if (SYMBOL_TABLE.getInstance().return_exist == false &&
//                SYMBOL_TABLE.getInstance().funcRetType != TYPE_VOID.getInstance()){
//            throw new SemanticException(line);
//        }`
        SYMBOL_TABLE.getInstance().endScope();

        SYMBOL_TABLE.getInstance().func_scope = false;
        SYMBOL_TABLE.getInstance().return_exist = false;
        /*********************************************************/
        /* [6] Return value is irrelevant for class declarations */
        /*********************************************************/
        return func_type;//null;
    }
}

