package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_BINOP extends AST_Node{
    public int OP;
    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_BINOP(int OP, int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== BINOP\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.OP = OP;
    }

    /*************************************************/
    /* The printing message for a binop exp AST node */
    /*************************************************/
    public void PrintMe()
    {
        String sOP= "";

        /*********************************/
        /* CONVERT OP to a printable sOP */
        /*********************************/
        if (OP == 0) {sOP = "+";}
        if (OP == 1) {sOP = "-";}
        if (OP == 2) {sOP = "*";}
        if (OP == 3) {sOP = "/";}
        if (OP == 4) {sOP = "<";}
        if (OP == 5) {sOP = ">";}
        if (OP == 6) {sOP = "=";}

        /*************************************/
        /* AST NODE TYPE = AST BINOP EXP */
        /*************************************/
        System.out.print("AST NODE BINOP \n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("BINOP(%s)",sOP));

    }
}
