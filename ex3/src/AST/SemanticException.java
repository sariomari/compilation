package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class SemanticException extends Exception
{
    public int line;

    public SemanticException(int line)
    {
        this.line = line;
    }
}