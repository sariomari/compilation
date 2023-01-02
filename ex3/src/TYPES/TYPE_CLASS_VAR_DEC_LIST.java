package TYPES;

public class TYPE_CLASS_VAR_DEC_LIST extends TYPE
{
    public TYPE_CLASS_VAR_DEC head;
    public TYPE_CLASS_VAR_DEC_LIST tail;


    public TYPE_CLASS_VAR_DEC_LIST(){
        this.head = null;
        this.tail = null;
    }

    public TYPE_CLASS_VAR_DEC_LIST(TYPE_CLASS_VAR_DEC head,TYPE_CLASS_VAR_DEC_LIST tail)
    {
        this.head = head;
        this.tail = tail;
    }

    public TYPE_CLASS_VAR_DEC_LIST clone(TYPE_CLASS_VAR_DEC_LIST list){
        TYPE_CLASS_VAR_DEC_LIST new_list = new TYPE_CLASS_VAR_DEC_LIST();
        TYPE_CLASS_VAR_DEC_LIST tmp1 = new_list;

        while(list != null){
            System.out.format("field:= %s - %s - %s",list.head.t, list.head.name, list.head.isMethod);
            TYPE_CLASS_VAR_DEC_LIST tmp2 = new TYPE_CLASS_VAR_DEC_LIST(new TYPE_CLASS_VAR_DEC(list.head.t, list.head.name, list.head.isMethod, 1), null);
            tmp1.tail = tmp2;
            list = list.tail;

        }
        return new_list.tail;
    }

    public int getType() {return 4;}
}
