package TYPES;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
    public TYPE t;
    public String name;
    public boolean isMethod;
    public int lineNumber;
    public TYPE_CLASS_VAR_DEC(TYPE t,String name, boolean isMethod, int lineNumber)
    {
        this.t = t;
        this.name = name;
        this.isMethod = isMethod;
        this.lineNumber = lineNumber;
    }
    public int getType() {return 3;}
}
