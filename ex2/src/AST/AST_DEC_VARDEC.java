package AST;

public class AST_DEC_VARDEC extends AST_DEC{
    public AST_VARDEC varDec;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_VARDEC(AST_VARDEC varDec)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.varDec = varDec;
    }

    /**************************************************/
    /* The printing message for a varDec dec AST node */
    /**************************************************/
    public void PrintMe()
    {
        /*******************************/
        /* RECURSIVELY PRINT varDec... */
        /*******************************/
        if (varDec != null) varDec.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "DEC\nVARDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (varDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
    }
}
