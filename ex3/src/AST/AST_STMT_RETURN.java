package AST;

public class AST_STMT_RETURN extends AST_STMT{
    public AST_EXP exp;

    public AST_STMT_RETURN(AST_EXP exp){
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.exp = exp;
    }

    /*********************************************************/
    /* The printing message for an return statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /*****************************/
        /* RECURSIVELY PRINT EXP ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "STMT\nRETURN");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }
}
