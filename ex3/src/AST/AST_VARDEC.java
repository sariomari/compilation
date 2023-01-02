package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VARDEC extends AST_Node {

    /*****************/
    /*  TYPE ID := exp */
    /*****************/
    public AST_TYPE type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP newExp;


    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_VARDEC(AST_TYPE type, String name, AST_EXP exp, AST_NEW_EXP newExp, int line)
    {
        this.line = ++line;
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if(newExp != null) 	System.out.print("====================== dec -> var ASSIGN NEW EXP SEMICOLON\n");
        else if (exp != null) 	System.out.print("====================== dec -> var ASSIGN exp SEMICOLON\n");
        else    System.out.print("====================== dec -> var SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.type= type;
        this.exp = exp;
        this.name = name;
        this.newExp = newExp;
    }

    public void PrintMe(){
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();
        if (newExp != null) newExp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ASSIGN\nVARDEC(%s)",name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
        if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        /**************************************/
        /* [1] Check That type is available */
        /**************************************/
        TYPE t1 = type.SemantMe();
        if (t1 == null)
        {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type.typeName);
            throw new SemanticException(line);
        }else{
            // checks validity of a type
            if(!t1.isArray() && !t1.isClass() && !(t1 == TYPE_STRING.getInstance()) && !(t1 == TYPE_INT.getInstance())){
                System.out.format(">> ERROR [%d:%d] return type does not exist: %s\n",6,6,t1.name);
                throw new SemanticException(line);
            }
        }

        /**************************************/
        /* [2] Check That Name does NOT exist in scope */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInScope(name, 1) != null)
        {
            System.out.format(">> ERROR [%d:%d] Name does exist in scope: %s\n",6,6,t1.name);
            throw new SemanticException(line);
        }

        TYPE t2 = null;
        if (exp != null) t2 = exp.SemantMe();
        else if (newExp != null) t2 = newExp.SemantMe();

        if (t2 != null)
        {
            // class A{}; class B extends A{};
            // A a = new B; or A a = new A;
            if (t1.isClass() && t2.isClass())
            {
                if (!(((TYPE_CLASS)t2).isAncestor((TYPE_CLASS)t1)) && t1 != t2)
                {
                    throw new SemanticException(line);
                }
            }
            // array arrayType = int[];
            //arrayType a := new int[5]; or arrayType a := nil;
            else if (t1.isArray())
            {
                if (!(((TYPE_ARRAY)t1).type == t2) && t2 != TYPE_NIL.getInstance())
                {
                    throw new SemanticException(line);
                }
            }
            else if (t1.isClass()) // A a := nil;
            {
                if (t2 != TYPE_NIL.getInstance())
                {
                    throw new SemanticException(line);
                }
            }
            else if (t1 != t2) // int x := "abc";
            {
                throw new SemanticException(line);
            }
        }
        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(name, t1, 1);

        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return t1;
    }

    public TYPE SemantMeForField() throws SemanticException{
        TYPE t2 = null;
        TYPE t1 = type.SemantMe();
        if (t1 == null)
        {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type.typeName);
            throw new SemanticException(line);
        }else{
            // checks validity of a type
            if(!t1.isArray() && !t1.isClass() && !(t1 == TYPE_STRING.getInstance()) && !(t1 == TYPE_INT.getInstance())){
                System.out.format(">> ERROR [%d:%d] return type does not exist: %s\n",6,6,t1.name);
                throw new SemanticException(line);
            }
        }

        /**************************************/
        /* [2] Check That Name does NOT exist in scope */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().findInScope(name, 1) != null)
        {
            System.out.format(">> ERROR [%d:%d] Name does exist in scope: %s\n",6,6,t1.name);
            throw new SemanticException(line);
        }

        if (exp != null) t2 = exp.SemantMe();
        else if (newExp != null) {
            System.out.format(">> ERROR [%d:%d] complex assign to : %s\n",6,6,name);
            throw new SemanticException(line);
        }

        if (t2 != null)
        {
            if ((t1.isClass() && t2 == TYPE_NIL.getInstance()) || (t1.isArray() && t2 == TYPE_NIL.getInstance())
            || (exp instanceof AST_EXP_INT && t2 == TYPE_INT.getInstance() && t1 == TYPE_INT.getInstance()) ||
               (exp instanceof AST_EXP_STRING && t2 == TYPE_STRING.getInstance() && t1 == TYPE_STRING.getInstance()))
            {
                /***************************************************/
                /* [3] Enter the Function Type to the Symbol Table */
                /***************************************************/
                SYMBOL_TABLE.getInstance().enter(name, t1, 1);

                /*********************************************************/
                /* [4] Return value is irrelevant for class declarations */
                /*********************************************************/
                return t1;
            }
            System.out.format(">> ERROR [%d:%d] complex assign to : %s\n",6,6,name);
            throw new SemanticException(line);
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(name, t1, 1);

        /*********************************************************/
        /* [4] Return value is irrelevant for class declarations */
        /*********************************************************/
        return t1;
    }
}
