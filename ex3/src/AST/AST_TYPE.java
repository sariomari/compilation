package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE extends AST_Node {
    public String typeName;
    int line;

    public AST_TYPE(String type, int line){
        this.line= ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/

        this.typeName = type;
    }

    @Override
    public void PrintMe() {

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE(%s)", typeName));
    }

    public TYPE SemantMe() throws SemanticException {
        TYPE t = null;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        if(typeName != null)
        {
            /** check that this is in fact declarable type (i.e. class, array, string, or int) **/
            /*********************************************************/
            /* [4] Return value is irrelevant for class declarations */
            /*********************************************************/
            if(typeName.equals("int")) {
                return TYPE_INT.getInstance();
            }
            else if(typeName.equals("string"))
                return TYPE_STRING.getInstance();
            else if(typeName.equals("void"))
                return TYPE_VOID.getInstance();

            t = SYMBOL_TABLE.getInstance().find(typeName, 0);
        }
        if(t == null || (!t.isClass() && !t.isArray())){
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,t);
            throw new SemanticException(this.line);
        }

        return t;
    }
}
