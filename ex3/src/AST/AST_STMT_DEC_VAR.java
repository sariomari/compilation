package AST;
import TYPES.*;

public class AST_STMT_DEC_VAR extends AST_STMT{
    public AST_VARDEC varDec;

    public AST_STMT_DEC_VAR(AST_VARDEC varDec, int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.varDec = varDec;
    }

    public void PrintMe(){
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (varDec != null) varDec.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STMT\nVARDEC"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (varDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        if (varDec != null) return varDec.SemantMe();

        return null;
    }
}
