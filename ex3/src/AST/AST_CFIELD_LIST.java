package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_LIST extends AST_Node {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_CFIELD head;
    public AST_CFIELD_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail, int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        if (head != null) head.PrintMe();
        if (tail != null) tail.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "CFIELD\nLIST\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
    }

    public TYPE SemantMe(/* head */) throws SemanticException{
        if(tail!=null){//not the last member of the c-field list
            int index1 = SYMBOL_TABLE.getInstance().index;
            SYMBOL_TABLE.getInstance().index++;
            SYMBOL_TABLE.getInstance().arr[index1] = (TYPE_CLASS_VAR_DEC)head.SemantMe();
            return new TYPE_CLASS_VAR_DEC_LIST(SYMBOL_TABLE.getInstance().arr[index1],(TYPE_CLASS_VAR_DEC_LIST)tail.SemantMe());
        }
        int index1 = SYMBOL_TABLE.getInstance().index;
        SYMBOL_TABLE.getInstance().arr[index1] = (TYPE_CLASS_VAR_DEC)head.SemantMe();
	SYMBOL_TABLE.getInstance().index++;
        return new TYPE_CLASS_VAR_DEC_LIST(SYMBOL_TABLE.getInstance().arr[index1],null);
    }
}
