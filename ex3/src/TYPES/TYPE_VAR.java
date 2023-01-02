package TYPES;

public class TYPE_VAR extends TYPE
{
    public String name;
    public String type;

    public TYPE_VAR(String name, String type)
    {
        this.type = type;
        this.name = name;
    }

    public int getType() {return 11;}
}