package TYPES;

import java.util.Objects;

public abstract class TYPE {
    public String name;
    public TypeEnum typeEnum;

    public boolean isClass(){return false;}
    public boolean isArray(){return false;}
    public boolean equals(Object given)
    {
        if (!(given instanceof TYPE)) return false;
        return (this.typeEnum == ((TYPE) given).typeEnum);
    }
}
