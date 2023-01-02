package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_DEC_VAR extends AST_CFIELD {
    public AST_VARDEC varDec;

    public AST_CFIELD_DEC_VAR (AST_VARDEC varDec, int line){
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

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (varDec != null) varDec.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CFIELD\nVARDEC"));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (varDec  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
    }
    
    public TYPE SemantMe() throws SemanticException
	{
        if (varDec != null){
            System.out.format("In class inserting " + varDec.name + "\n");
            return new TYPE_CLASS_VAR_DEC(varDec.SemantMeForField(), varDec.name, false, line);
        }
        return null;
	}
}
