package AST;

public class AST_FUNCDEC extends AST_Node {
    public AST_TYPE type;
    public String name;
    public AST_ARG_LIST args;
    public AST_STMT_LIST lst;

    public AST_FUNCDEC(AST_TYPE type, String name, AST_ARG_LIST args, AST_STMT_LIST lst){
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.args = args;
        this.name = name;
        this.lst = lst;
        this.type = type;

    }
    public void PrintMe(){
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null) type.PrintMe();
        if (lst != null) lst.PrintMe();
        if (args != null) args.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNCDEC(%s)",name)
        );

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (args != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,args.SerialNumber);
        if (lst != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,lst.SerialNumber);
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }
}

