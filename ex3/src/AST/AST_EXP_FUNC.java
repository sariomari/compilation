package AST;

public class AST_EXP_FUNC extends AST_EXP{
    public AST_VAR v;
    public String name;
    public AST_EXP_LIST args;

    public AST_EXP_FUNC(AST_VAR v,String name, AST_EXP_LIST args, int line ) {
        this.line = line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.v = v;
        this.name = name;
        this.args = args;

    }


    /*************************************************/
    /* The printing message for a binop exp AST node */
    /*************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* RECURSIVELY PRINT left + right ... */
        /**************************************/
        if (v != null) v.PrintMe();
        if (args != null) args.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP\nFUNC(%s)",name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (args  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,args.SerialNumber);
        if (v  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
    }
}