package TYPES;

public class TYPE_CLASS extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE_CLASS father;

    /**************************************************/
    /* Gather up all data members in one place        */
    /* Note that data members coming from the AST are */
    /* packed together with the class methods         */
    /**************************************************/
    public TYPE_CLASS_VAR_DEC_LIST data_members;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_CLASS_VAR_DEC_LIST data_members)
    {
        this.name = name;
        this.father = father;
        this.data_members = data_members;
    }
    public TYPE findMembers(String name, boolean isMethod) {
        TYPE_CLASS_VAR_DEC_LIST temp;
        TYPE_CLASS_VAR_DEC tempHead;
        TYPE_CLASS tempFather = this;
        temp = null;

        while(tempFather != null){
            temp = tempFather.data_members;
            while(temp != null){
                tempHead = temp.head;
                System.out.println("Comparing " + tempHead.name + " to " + name);
                if(tempHead.name.equals(name)){
                    if(tempHead.isMethod == isMethod){
                        return tempHead.t;
                    }
                }
                temp = temp.tail;
            }
            tempFather = tempFather.father;
        }
        return null;
    }

    public static TYPE findMembers_test(TYPE_CLASS cp, String name, boolean isMethod){
        TYPE_CLASS_VAR_DEC_LIST temp;
        TYPE_CLASS_VAR_DEC tempHead;
        TYPE_CLASS tempFather = cp;
        temp = null;

        while(tempFather != null){
            temp = tempFather.data_members;
            while(temp != null){
                tempHead = temp.head;
                System.out.println("Comparing " + tempHead.name + " to " + name);
                if(tempHead.name.equals(name)){
                    if(tempHead.isMethod == isMethod){
                        return tempHead.t;
                    }
                }
                temp = temp.tail;
            }
            tempFather = tempFather.father;
        }
        return null;
    }

    public boolean isAncestor(TYPE_CLASS actualType)
    {
        TYPE_CLASS tempFather = this;
        while(tempFather != null){
            System.out.println("in isAncestor "+ tempFather.name);
            if (tempFather.name.equals(actualType.name)){
                return true;
            }
            tempFather = tempFather.father;
        }
        return false;
    }

    public boolean isClass(){ return true;}


    public int getType() {return 2;}
}
