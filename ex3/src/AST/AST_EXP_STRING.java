package AST;
import TYPES.*;
public class AST_EXP_STRING extends AST_EXP {
    public String str;


    public AST_EXP_STRING(String str){
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
                String.format("EXP\nSTRING(%s)",str));
    }

    public TYPE SemantMe() throws SemanticException{
        return TYPE_STRING.getInstance();
    }
}
