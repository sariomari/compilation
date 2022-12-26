package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARRAYTYPEDEF extends AST_DEC{
    public String name;
    public AST_TYPE type;

    public AST_ARRAYTYPEDEF(String name, AST_TYPE type, int line)
    {
        this.line = line;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.type = type;
        this.name = name;
    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null) type.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ARRAY\nTYPEDEF(%s)", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException{

        /* Make sure that array declaration is in the global scope */
        String curr_scope = SYMBOL_TABLE.getInstance().findCurrentScope();
        if (!(curr_scope.equals("global")))
        {
            throw new SemanticException(line);
        }

        /* Make sure that the array name is not found in previous declarations */
        if (SYMBOL_TABLE.getInstance().findInCurrentScope(name) != null)
        {
            throw new SemanticException(line);
        }

        /* Call type.SemantMe() */
        TYPE_ARRAY type_semant = new TYPE_ARRAY(name, type.SemantMe());
        SYMBOL_TABLE.getInstance().enter(name, type_semant, true, SYMBOL_TABLE.getInstance().current_scope);

        return type_semant;

    }
}

