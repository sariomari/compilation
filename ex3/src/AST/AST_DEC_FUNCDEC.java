package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_FUNCDEC extends AST_DEC{
    public AST_FUNCDEC funcDec;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_FUNCDEC(AST_FUNCDEC funcDec, int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.funcDec = funcDec;

    }

    /***************************************************/
    /* The printing message for a funcDec dec AST node */
    /***************************************************/
    public void PrintMe()
    {
        /********************************/
        /* RECURSIVELY PRINT funcDec... */
        /********************************/
        if (funcDec != null) funcDec.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "DEC\nFUNCDEC");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (funcDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
    }
    public TYPE SemantMe() throws SemanticException
    {
        return funcDec.SemantMe();
    }
}
