package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_STRING extends AST_EXP {
    public String str;


    public AST_EXP_STRING(String str, int line){
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.str = str;
    }

    public void PrintMe(){
        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("EXP\nSTRING(%s)",str.replace('"','\'')));
    }

    public TYPE SemantMe() throws SemanticException{
        return TYPE_STRING.getInstance();
    }
}
