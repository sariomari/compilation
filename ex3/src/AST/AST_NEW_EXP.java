package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_NEW_EXP extends AST_EXP {
    /*****************/
    /*  NEW type [exp] */
    /*****************/
    public AST_TYPE type;
    public AST_EXP exp;

    public AST_NEW_EXP(AST_TYPE type, AST_EXP exp, int line) {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (exp != null) System.out.print("====================== new exp -> NEW ID [exp]\n");
        if (exp == null) System.out.print("====================== new exp -> NEW ID\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type = type;
        this.exp = exp;
    }

    public void PrintMe() {
        /************************************/
        /* RECURSIVELY PRINT TYPE + EXP ... */
        /************************************/
        if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("NEWEXP")
        );

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException {
        TYPE t1 = null;
        TYPE t2 = null;
        System.out.println("name: " + type + " exp: " + exp);
        if (type != null) t1 = type.SemantMe();
        if (exp != null) t2 = exp.SemantMe();

        if (t1 == null) {
            throw new SemanticException(line);
        }

        if (exp == null) {
            // A a = new A;
            // int a = new int; not working
            // string a = new string; not working

            // !((t1.isClass() || t1 == TYPE_INT.getInstance() || t1 == TYPE_STRING.getInstance()))
            if (!t1.isClass()) {
                throw new SemanticException(line);
            }
            return t1;

        } else /* if (exp != null) */ {
            // array arrayType = new int[];
            // array arrayType2 = new arrayType[];
            // arrayType arr1 = new int[6];
            // arrayType arr2 = new int[x];
            // arrayType2 arr3 = new arrayType[6];
            if (!((t1.isArray() || t1.isClass() || t1 == TYPE_INT.getInstance() || t1 == TYPE_STRING.getInstance()) && t2 == TYPE_INT.getInstance())) {
                throw new SemanticException(line);
            }

//            if (!(exp instanceof AST_EXP_INT && ((AST_EXP_INT) exp).value > 0)) {
//                throw new SemanticException(line);
//            }

            if (exp instanceof AST_EXP_INT) {
                if (((AST_EXP_INT) exp).value <= 0) {
                    throw new SemanticException(line);
                }
            }
            return t1;
        }
    }
}
