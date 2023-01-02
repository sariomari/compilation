package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_FUNCCALL extends AST_STMT
{
    /************************/
    /* simple variable name */
    /************************/
    public String name;
    public AST_VAR var;
    public AST_EXP_LIST expList;
    public int method_id;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_STMT_FUNCCALL(String name, AST_VAR var, AST_EXP_LIST expList, int line)
    {
        this.line = ++line;
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
        this.name = name;
        this.var = var;
        this.expList = expList;
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
        if (name != null) System.out.format("function %s", name);
        if (var != null) var.PrintMe();
        if (expList != null) expList.PrintMe();

        /*************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /*************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNCCALL(%s)",name));

        /**************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /**************/
        if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, expList.SerialNumber);
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        TYPE t2 = null;

        TYPE_CLASS cur_class = SYMBOL_TABLE.getInstance().curr_class;
        if (cur_class != null && var == null){
            if (cur_class.father != null){
                t2 = TYPE_CLASS.findMembers_test(cur_class.father, name, true);

                if (t2 != null){
                    TYPE_LIST exp_params = ((TYPE_FUNCTION)t2).params;
                    AST_EXP_LIST actual_params = expList;
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

        if(var != null) {
            TYPE t_seman = SYMBOL_TABLE.getInstance().find(var.SemantMe().name, 1);
            if(!(t_seman instanceof TYPE_CLASS))
            {
                throw new SemanticException(line);
            }

            t2 = ((TYPE_CLASS)t_seman).findMembers(name, true);

        }else {
            if(name != null) t2 = SYMBOL_TABLE.getInstance().find(name, 1);
        }

        if(t2 == null){
            throw new SemanticException(line);
        }
        if(!t2.isFunc()){
            throw new SemanticException(line);
        }

        TYPE_LIST parmstypes = ((TYPE_FUNCTION)t2).params;
        AST_EXP_LIST p = expList;

        while(p != null && parmstypes != null){
            AST_EXP parm = p.head;
            TYPE exp_p = parmstypes.head;
            TYPE act_p = parm.SemantMe();

            if (exp_p.isClass() && act_p == TYPE_NIL.getInstance()){

            }
            else if (exp_p.isArray() && act_p == TYPE_NIL.getInstance()){

            }
            else if(exp_p.isClass() && act_p.isClass() && ((TYPE_CLASS)act_p).isAncestor((TYPE_CLASS)exp_p)){

            }
            else if(exp_p.isArray() && act_p.isArray() && ((TYPE_ARRAY)exp_p).name == ((TYPE_ARRAY)act_p).name){

            }
            else if( exp_p != act_p ){
                throw new SemanticException(line);
            }

            p = p.tail;
            parmstypes = parmstypes.tail;
        }

        if (p != null || parmstypes != null) {
            throw new SemanticException(line);
        }
        return ((TYPE_FUNCTION)t2).returnType;
    }
}
