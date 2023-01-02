package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_RETURN extends AST_STMT{
    public AST_EXP exp;
    private String epilogue_label;

    public AST_STMT_RETURN(AST_EXP exp, int line){
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.exp = exp;
    }

    /*********************************************************/
    /* The printing message for an return statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /*****************************/
        /* RECURSIVELY PRINT EXP ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "STMT\nRETURN");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }
    public TYPE SemantMe() throws SemanticException
    {
        TYPE t = null;
        TYPE expec_type = SYMBOL_TABLE.getInstance().funcRetType;

        if (exp != null){
            t = exp.SemantMe();

            if( expec_type.isClass() && ((t.isClass()  && ((TYPE_CLASS)expec_type).isAncestor((TYPE_CLASS)t)) || t == TYPE_NIL.getInstance())
                    && SYMBOL_TABLE.getInstance().func_scope ){
                SYMBOL_TABLE.getInstance().return_exist = true;
                return null;
            }
            if( expec_type.isArray() && ((t.isArray() && ((TYPE_ARRAY)t).type == ((TYPE_ARRAY)expec_type).type)
                    || t == TYPE_NIL.getInstance())
                    && SYMBOL_TABLE.getInstance().func_scope && ((TYPE_ARRAY)t).name.equals(((TYPE_ARRAY)expec_type).name)){
                SYMBOL_TABLE.getInstance().return_exist = true;
                return null;
            }
            if( expec_type == t
                    && SYMBOL_TABLE.getInstance().func_scope ){
                SYMBOL_TABLE.getInstance().return_exist = true;
                return null;
            }
            throw new SemanticException(line);

        }
        else if (exp == null){
            if(SYMBOL_TABLE.getInstance().funcRetType == TYPE_VOID.getInstance() && SYMBOL_TABLE.getInstance().func_scope) {
                SYMBOL_TABLE.getInstance().return_exist = true;
                return null;
            }
        }
        throw new SemanticException(line);
    }
}
