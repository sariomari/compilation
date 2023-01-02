package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_FUNC extends AST_EXP{
    /*****************************/
    /*  caller.methodName([params]*) */
    /*  methodName([params]*) */
    /*****************************/
    public AST_VAR caller;
    public String func;
    public AST_EXP_LIST params;

    public AST_EXP_FUNC(AST_VAR caller,String func, AST_EXP_LIST params, int line ) {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.caller = caller;
        this.func = func;
        this.params = params;

    }


    /*************************************************/
    /* The printing message for a binop exp AST node */
    /*************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* RECURSIVELY PRINT left + right ... */
        /**************************************/
        if (caller != null) caller.PrintMe();
        if (params != null) params.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP\nFUNC(%s)",func));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (params  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
        if (caller  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,caller.SerialNumber);
    }

    //int m1(){ }
    //  int m2(){
    //	    int m1;
    //	    m1();
    //  }
    //}
    public TYPE SemantMe() throws SemanticException {
        TYPE t2 = null;

        TYPE_CLASS cur_class = SYMBOL_TABLE.getInstance().curr_class;
        if (cur_class != null && caller == null){
            if (cur_class.father != null){
                t2 = TYPE_CLASS.findMembers_test(cur_class.father, func, true);

                if (t2 != null){
                    TYPE_LIST exp_params = ((TYPE_FUNCTION)t2).params;
                    AST_EXP_LIST actual_params = params;
                    while (actual_params != null && exp_params != null){
                        TYPE exp_p = exp_params.head;
                        TYPE act_p = (actual_params.head).SemantMe();

                        // class A{}
                        // class B extends A{}
                        // m1(A a, int x);
                        // m1(a, 5);
                        // m1(b, 5);
                        if (exp_p.isClass() && act_p == TYPE_NIL.getInstance()){

                        }
                        else if (exp_p.isArray() && act_p == TYPE_NIL.getInstance()){

                        }
                        else if(exp_p.isClass() && act_p.isClass() && ((TYPE_CLASS)act_p).isAncestor((TYPE_CLASS)exp_p)){

                        }
                        else if(exp_p.isArray() && act_p.isArray() && ((TYPE_ARRAY)exp_p).type == ((TYPE_ARRAY)act_p).type){

                        }
                        else if( exp_p != act_p ){
                            throw new SemanticException(line);
                        }
                        actual_params = actual_params.tail;
                        exp_params = exp_params.tail;
                    }
                    if(actual_params != null || exp_params != null){
                        throw new SemanticException(line);
                    }
                    return ((TYPE_FUNCTION)t2).returnType;
                }
            }

        }

        if(caller != null) { // caller.methodName(params)
            TYPE t_seman = SYMBOL_TABLE.getInstance().find(caller.SemantMe().name, 1);
            if(!(t_seman instanceof TYPE_CLASS))
            {
                throw new SemanticException(line);
            }

            t2 = ((TYPE_CLASS)t_seman).findMembers(func, true);

        }else {
            t2 = SYMBOL_TABLE.getInstance().find(func, 1);
        }

        if(t2 == null){
            throw new SemanticException(line);
        }
        if(!t2.isFunc()){
            throw new SemanticException(line);
        }

        TYPE_LIST exp_params = ((TYPE_FUNCTION)t2).params;
        AST_EXP_LIST actual_params = params;
        while (actual_params != null && exp_params != null){
            TYPE exp_p = exp_params.head;
            TYPE act_p = (actual_params.head).SemantMe();

            // class A{}
            // class B extends A{}
            // m1(A a, int x);
            // m1(a, 5);
            // m1(b, 5);
            if (exp_p.isClass() && act_p == TYPE_NIL.getInstance()){

            }
            else if (exp_p.isArray() && act_p == TYPE_NIL.getInstance()){

            }
            else if(exp_p.isClass() && act_p.isClass() && ((TYPE_CLASS)act_p).isAncestor((TYPE_CLASS)exp_p)){

            }
            else if(exp_p.isArray() && act_p.isArray() && ((TYPE_ARRAY)exp_p).type == ((TYPE_ARRAY)act_p).type){

            }
            else if( exp_p != act_p ){
                throw new SemanticException(line);
            }
            actual_params = actual_params.tail;
            exp_params = exp_params.tail;
        }
        if(actual_params != null || exp_params != null){
            throw new SemanticException(line);
        }
        return ((TYPE_FUNCTION)t2).returnType;
    }
}
