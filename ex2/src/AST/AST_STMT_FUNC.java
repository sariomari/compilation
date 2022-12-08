package AST;

public class AST_STMT_FUNC extends AST_STMT{
    public String name;
    public AST_EXP_LIST args;
    public AST_VAR v;

    public AST_STMT_FUNC(String name, AST_EXP_LIST args, AST_VAR v){
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.name = name;
        this.args = args;
        this.v = v;
    }

    public void PrintMe(){
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
                String.format("STMT\nFUNC(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (args  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,args.SerialNumber);
        if (v  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
    }
}
