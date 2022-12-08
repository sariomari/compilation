package AST;

public class AST_TYPE extends AST_Node {
    public String type;

    public AST_TYPE(String type){
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/

        this.type = type;
    }

    @Override
    public void PrintMe() {

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE(%s)",type));
    }
}
