package AST;

public class AST_EXP_LEXPR extends AST_EXP {
    public AST_EXP exp;

    public AST_EXP_LEXPR(AST_EXP exp){
        this.exp = exp;
    }

    /*************************************************/
    /* The printing message for a paren exp AST node */
    /*************************************************/
    public void PrintMe()
    {
        /*****************************/
        /* RECURSIVELY PRINT exp ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "EXP\nPAREN");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/

    }
}