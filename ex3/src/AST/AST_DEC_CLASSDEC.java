package AST;
import TYPES.*;

public class AST_DEC_CLASSDEC extends AST_DEC{
    public AST_CLASSDEC classDec;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_DEC_CLASSDEC(AST_CLASSDEC classDec,int line)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.classDec = classDec;
        this.line = line;
    }

    /****************************************************/
    /* The printing message for a classDec dec AST node */
    /****************************************************/
    public void PrintMe()
    {
        /*********************************/
        /* RECURSIVELY PRINT classDec... */
        /*********************************/
        if (classDec != null) classDec.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "DEC\nCLASSDEC\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (classDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,classDec.SerialNumber);
    }
    public TYPE SemantMe() throws SemanticException
    {
        return classDec.SemantMe();
    }
}
