package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CLASSDEC extends AST_DEC{
    /*****************/
    /*  CLASS ID [EXTENDS ID] {[cField]+} */
    /*****************/
    public String name;
    public String parentClass;
    public AST_CFIELD_LIST body;

    public AST_CLASSDEC(String name, String parentClass, AST_CFIELD_LIST body, int line)
    {
        this.line = ++line;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.parentClass = parentClass;
        this.name = name;
        this.body = body;

    }

    /*********************************************************/
    /* The printing message for an assign statement AST node */
    /*********************************************************/
    public void PrintMe()
    {
        /***********************************/
        /* RECURSIVELY PRINT VAR + EXP ... */
        /***********************************/
        if(body != null) body.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        if(parentClass != null){
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("CLASSDEC\n(%s,%s)",name,parentClass)
            );
        } else{
            AST_GRAPHVIZ.getInstance().logNode(
                    SerialNumber,
                    String.format("CLASSDEC\n(%s)",name)
            );
        }


        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if(body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }

    public TYPE SemantMe() throws SemanticException
    {
        // sorry when we implemented the code it was at 3 o-clock ,
        // and we are so tired, so the code is rubbish.
        TYPE parent_type = null;

        if (SYMBOL_TABLE.getInstance().getScope() != 0){
            System.out.format(">> ERROR [%d:%d] defining class outside global scope := exp\n",6,6);
            throw new SemanticException(line);
        }

        if(parentClass != null){
            parent_type = SYMBOL_TABLE.getInstance().find(parentClass, 0);
            if (parent_type == null || !parent_type.isClass()){
                System.out.format(">> ERROR [%d:%d] non existing class type to extend %s\n",2,2,parentClass);
                throw new SemanticException(line);
            }
        }

        if (SYMBOL_TABLE.getInstance().find(name, 1) != null){
            System.out.format(">> ERROR [%d:%d] name %s already exists in scope\n",2,2,name);
            throw new SemanticException(line);
        }

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        TYPE_CLASS current_class_type = null;
        TYPE_CLASS_VAR_DEC_LIST parent_members = null;
        if (parentClass != null) {
            parent_members = ((TYPE_CLASS)parent_type).data_members;
//            TYPE_CLASS_VAR_DEC_LIST clone_parent_members = parent_members.clone(parent_members);
            current_class_type = new TYPE_CLASS((TYPE_CLASS) parent_type, name, null);

        }
        if (parentClass == null) {
            current_class_type = new TYPE_CLASS(
                    null, name, null);

        }
        // To enable a data member of the same class
        SYMBOL_TABLE.getInstance().enter(name, current_class_type, 0);
        SYMBOL_TABLE.getInstance().curr_class = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(name, 0);
        SYMBOL_TABLE.getInstance().class_scope = true;
        SYMBOL_TABLE.getInstance().beginScope();
        /***************************/
        /* [2] Semant Data Members */
        /***************************/

//        current_class_type.data_members = ((TYPE_CLASS_VAR_DEC_LIST)body.SemantMe());
        if(parentClass == null){
            current_class_type.data_members = (TYPE_CLASS_VAR_DEC_LIST)body.SemantMe();
        }else{
            current_class_type.data_members = (TYPE_CLASS_VAR_DEC_LIST)body.SemantMe();
        }

        SYMBOL_TABLE.getInstance().arr = new TYPE_CLASS_VAR_DEC[100];
        SYMBOL_TABLE.getInstance().index = 0;
        TYPE_CLASS_VAR_DEC_LIST tmp_data_members;
        TYPE_CLASS_VAR_DEC curr_member_in_current_class;


        if (parentClass != null){
            System.out.format("check if inherit right of class %s from class %s\n", name, parentClass);
            TYPE_CLASS curr = SYMBOL_TABLE.getInstance().curr_class;
            tmp_data_members = current_class_type.data_members;
            while (tmp_data_members != null){
//                curr_member_in_current_class = current_class_type.data_members.head;
                curr_member_in_current_class = tmp_data_members.head;
                TYPE b = SYMBOL_TABLE.getInstance().findInScope(curr_member_in_current_class.name, 1);

                if (b instanceof TYPE_FUNCTION){
                    TYPE member_type_in_parent = TYPE_CLASS.findMembers_test(curr.father, curr_member_in_current_class.name, false);
                    if (member_type_in_parent != null){
                        throw new SemanticException(curr_member_in_current_class.lineNumber);
                    }
                    member_type_in_parent = TYPE_CLASS.findMembers_test(curr.father, curr_member_in_current_class.name, true);
                    if (member_type_in_parent != null){

//                        System.out.format("check if is same return type");
//                        check that the reutrn type is the same in the son and parent class in method nameFunc
                        if( ((TYPE_FUNCTION)member_type_in_parent).returnType != ((TYPE_FUNCTION)b).returnType ){
                            throw new SemanticException(curr_member_in_current_class.lineNumber);
                        }

                        TYPE_LIST exp_params = ((TYPE_FUNCTION)member_type_in_parent).params;
                        TYPE_LIST act_params = ((TYPE_FUNCTION)b).params;

                        while(act_params != null && exp_params != null){
                            System.out.format("check if is same method");

                            TYPE act_p = act_params.head;
                            TYPE exp_p = exp_params.head;

                            if(exp_p.isClass()){
                                if(!(act_p.isClass() && ((TYPE_CLASS)act_p).name == ((TYPE_CLASS)exp_p).name)){
                                    throw new SemanticException(curr_member_in_current_class.lineNumber);
                                }
                            }else if(exp_p.isArray()) {
                                if(!(act_p.isArray() && ((TYPE_ARRAY)act_p).name == ((TYPE_ARRAY)exp_p).name)){
                                    throw new SemanticException(curr_member_in_current_class.lineNumber);
                                }
                            } else{
                                if(act_p != exp_p){
                                    throw new SemanticException(curr_member_in_current_class.lineNumber);
                                }
                            }
                            act_params = act_params.tail;
                            exp_params = exp_params.tail;
                        }

                        if (act_params != null || exp_params != null) {
                            throw new SemanticException(curr_member_in_current_class.lineNumber);
                        }
                    }
                }
                else {
                    TYPE member_type_in_parent = TYPE_CLASS.findMembers_test(curr.father, curr_member_in_current_class.name, false);
                    if(member_type_in_parent != null){
                        throw new SemanticException(curr_member_in_current_class.lineNumber);
                    }
                    member_type_in_parent = TYPE_CLASS.findMembers_test(curr.father, curr_member_in_current_class.name, true);
                    if(member_type_in_parent != null){
                        throw new SemanticException(curr_member_in_current_class.lineNumber);
                    }
                    
                }
                tmp_data_members = tmp_data_members.tail;
//		    current_class_type.data_members = current_class_type.data_members.tail;
            }
        }

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();
        SYMBOL_TABLE.getInstance().class_scope = false;
        SYMBOL_TABLE.getInstance().curr_class = null;

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
//        SYMBOL_TABLE.getInstance().enter(name, current_class_type, 0);
//        System.out.format("Class " + name + " has fields: ");


        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }
}
