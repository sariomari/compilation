package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE extends AST_Node {
    public String typeName;
    int line;

    public AST_TYPE(String type, int line){
        this.line= line;
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


}
