package AST;

public class AST_PROGRAM extends AST_Node{
    public AST_DEC d;
    public AST_PROGRAM p;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_PROGRAM(AST_DEC d, AST_PROGRAM p)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.d = d;
        this.p = p;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /*********************************/
        /* RECURSIVELY PRINT DECLIST ... */
        /*********************************/
        if (d != null) d.PrintMe();
        if (p != null) p.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "PROGRAM\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (d != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,d.SerialNumber);
        if (p != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,p.SerialNumber);
    }

}
