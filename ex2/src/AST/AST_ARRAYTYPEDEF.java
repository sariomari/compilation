package AST;

// extends AST_NODE
public class AST_ARRAYTYPEDEF extends AST_Node{
    public String name;
    public AST_TYPE type;

    public AST_ARRAYTYPEDEF(String name, AST_TYPE type)
    {

        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.type = type;
        this.name = name;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null) type.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ARRAY\nTYPEDEF(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }
}

