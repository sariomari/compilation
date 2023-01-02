package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
public class AST_ARG extends AST_Node {
    public AST_TYPE type;
    public String name;


    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_ARG(AST_TYPE type, String name,int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.name = name;
        this.type = type;
    }

    /************************************************/
    /* The printing message for an INT EXP AST node */
    /************************************************/
    public void PrintMe()
    {
        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ARGUMENT type %s\n",name)
        );
    }
}
