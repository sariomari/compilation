package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARRAYTYPEDEF extends AST_DEC{
    /*****************/
    /*  ARRAY ID = type[] */
    /*****************/
    public String name;
    public AST_TYPE type;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_ARRAYTYPEDEF(String name, AST_TYPE type, int line)
    {
        this.line = ++line;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
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
        if (SYMBOL_TABLE.getInstance().getScope() != 0)
        {
            System.out.format(">> ERROR [%d:%d] defining array outside global scope := exp\n",6,6);
            throw new SemanticException(line);
        }

        TYPE t1 = null;
        TYPE t2 = null;

//        if (name != null) t1 = SYMBOL_TABLE.getInstance().find(name, 0);
        if (name != null) t1 = SYMBOL_TABLE.getInstance().find(name, 1);
        if (type != null) t2 = type.SemantMe();

        if (t1 != null)
        {
            System.out.format(">> ERROR [%d:%d] type already exists := exp\n",6,6);
            throw new SemanticException(line);
        }

        if (t2 == null)
        {
            System.out.format(">> ERROR [%d:%d] type doesn't exist := exp\n",6,6);
            throw new SemanticException(line);
        }

        if (t2.isClass() || t2.isArray() || t2 == TYPE_INT.getInstance() || t2 == TYPE_STRING.getInstance()){
            TYPE_ARRAY ta = new TYPE_ARRAY(name, t2);
            System.out.println(ta + " name: " + ta.name + " type: " + ta.type);
            SYMBOL_TABLE.getInstance().enter(name, ta, 0);//new TYPE_ARRAY(name, type));
        } else {
            System.out.format(">> ERROR [%d:%d] can't make array from this type := exp\n",6,6);
            throw new SemanticException(line);

        }

        return null;
    }
}

