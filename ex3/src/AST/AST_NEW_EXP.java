package AST;

public class AST_NEW_EXP extends AST_EXP {
    public AST_TYPE type;
    public AST_EXP exp;

    public AST_NEW_EXP(AST_TYPE type, AST_EXP exp){
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type = type;
        this.exp = exp;
    }

    public void PrintMe(){
        /************************************/
        /* RECURSIVELY PRINT TYPE + EXP ... */
        /************************************/
        if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("NEWEXP")
        );

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }
}
