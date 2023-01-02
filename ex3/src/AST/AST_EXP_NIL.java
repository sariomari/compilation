package AST;
import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_EXP_NIL extends AST_EXP
{


    public AST_EXP_NIL(int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

    }


    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/
        System.out.print("AST NODE EXP NIL\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "EXP NIL\n");


    }
    public TYPE SemantMe()
    {
        return TYPE_NIL.getInstance();
    }

}