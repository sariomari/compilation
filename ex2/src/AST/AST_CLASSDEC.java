package AST;

public class AST_CLASSDEC extends AST_DEC{
    public String name;
    public String parentClass;
    public AST_CFIELD_LIST lst;

    public AST_CLASSDEC(String name, String parentClass, AST_CFIELD_LIST lst)
    {


        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.parentClass = parentClass;
        this.name = name;
        this.lst=lst;

    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if(lst!= null) lst.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        if(parentClass != null){
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("CLASSDEC\n(%s,%s)",name,parentClass)
            );
        } else{
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("CLASSDEC\n(%s)",name)
            );
        }


        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(lst!= null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,lst.SerialNumber);
    }
}