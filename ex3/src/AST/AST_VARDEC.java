package AST;

public class AST_VARDEC extends AST_Node {
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP newExp;

    public AST_VARDEC(AST_TYPE type, String name, AST_EXP exp, AST_NEW_EXP newExp){

        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.type= type;
        this.exp = exp;
        this.name = name;
        this.newExp = newExp;
    }
    public void PrintMe(){
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();
        if (newExp != null) newExp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ASSIGN\nVARDEC(%s)",name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
        if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
    }
}
