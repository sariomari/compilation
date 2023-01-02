/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.lang.Math;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
    private int hashArraySize = 15;
    private int scopeDepth = 0;

    /**********************************************/
    /* The actual symbol table data structure ... */
    /**********************************************/
    private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
    private SYMBOL_TABLE_ENTRY top;
    private int top_index = 0;
    public boolean class_scope = false;
    public boolean func_scope = false;
    public boolean return_exist = false;
    public TYPE funcRetType;
    public TYPE_CLASS curr_class = null;
    public int lineNumber = 0;

    public TYPE_CLASS_VAR_DEC[] arr = new TYPE_CLASS_VAR_DEC[100];
    public int index = 0;

    /*********************************************/
    /* Hash function for exposition purposes ... */
    /*********************************************/
    private int hash(String s)
    {
        return (s.hashCode() % hashArraySize + hashArraySize) % hashArraySize; // Handling negative numbers
    }


    public int getScope()
    {
        return scopeDepth;
    }


    /****************************************************************************/
    /* Enter a variable, function, class type or array type to the symbol table */
    /****************************************************************************/
    public void enter(String name, TYPE t, int kind)
    {
        /*************************************************/
        /* [1] Compute the hash value for this new entry */
        /*************************************************/
        int hashValue = hash(name);

        /******************************************************************************/
        /* [2] Extract what will eventually be the next entry in the hashed position  */
        /*     NOTE: this entry can very well be null, but the behaviour is identical */
        /******************************************************************************/
        SYMBOL_TABLE_ENTRY next = table[hashValue];

        /**************************************************************************/
        /* [3] Prepare a new symbol table entry with name, type, next and prevtop */
        /**************************************************************************/
        SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++, kind);

        /**********************************************/
        /* [4] Update the top of the symbol table ... */
        /**********************************************/
        top = e;

        /****************************************/
        /* [5] Enter the new entry to the table */
        /****************************************/
        table[hashValue] = e;

        /**************************/
        /* [6] Print Symbol Table */
        /**************************/
        PrintMe();

        System.out.println("/*****************************************/");
        System.out.println("/** enter "+ name + "  " + t.name+ "  ***/");
        System.out.println("/*****************************************/");
    }


    /***********************************************/
    /* Find the inner-most scope element with name */
    /***********************************************/
    public TYPE find(String name, int kind)
    {
        SYMBOL_TABLE_ENTRY e;
        if (kind == 0){
            for (e = table[hash(name)]; e != null; e = e.next)
            {
                if (name.equals(e.name))
                {
                    if (e.kind == kind){
                        System.out.println("end find with " + e.name);
                        return e.type;
                    } else{
                        return null;
                    }
                }
            }
            System.out.println("end find without result");
        }
        else // kind == 1
        {
            for (e = table[hash(name)]; e != null; e = e.next)
            {
                if (name.equals(e.name))
                {
                    System.out.println("end find with " + e.name);

                    return e.type;
                }
            }
            System.out.println("end find without result");
        }
        return null;
    }


    /***********************************************/
    /* Find if element name exists in scope */
    /***********************************************/
    public TYPE findInScope(String name, int kind)
    {
        SYMBOL_TABLE_ENTRY e;

        for (e = table[top.index]; e != null && e.name != "SCOPE-BOUNDARY"; e = e.prevtop) {
            if (name.equals(e.name)) {
                return e.type;
            }
        }

        return null;
    }

    /***************************************************************************/
    /* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
    /***************************************************************************/
    public void beginScope()
    {
        scopeDepth++;
        /************************************************************************/
        /* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
        /* they are not really types. In order to be ablt to debug print them,  */
        /* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
        /* class only contain their type name which is the bottom sign: _|_     */
        /************************************************************************/
        enter(
                "SCOPE-BOUNDARY",
                new TYPE_FOR_SCOPE_BOUNDARIES(null), 2);

        /*********************************************/
        /* Print the symbol table after every change */
        /*********************************************/
        PrintMe();
    }

    /********************************************************************************/
    /* end scope = Keep popping elements out of the data structure,                 */
    /* from most recent element entered, until a <NEW-SCOPE> element is encountered */
    /********************************************************************************/
    public void endScope()
    {
        /**************************************************************************/
        /* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */
        /**************************************************************************/
        while (top.name != "SCOPE-BOUNDARY")
        {
            table[top.index] = top.next;
            top_index = top_index-1;
            top = top.prevtop;
        }
        /**************************************/
        /* Pop the SCOPE-BOUNDARY sign itself */
        /**************************************/
        table[top.index] = top.next;
        top_index = top_index-1;
        top = top.prevtop;

        /*********************************************/
        /* Print the symbol table after every change */
        /*********************************************/
        scopeDepth--;
        System.out.println("end SCOPE-BOUNDARY");
        PrintMe();
    }

    public static int n=0;

    public void PrintMe()
    {
        int i=0;
        int j=0;
        String dirname="./output/";
        String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

        try
        {
            /*******************************************/
            /* [1] Open Graphviz text file for writing */
            /*******************************************/
            PrintWriter fileWriter = new PrintWriter(dirname+filename);

            /*********************************/
            /* [2] Write Graphviz dot prolog */
            /*********************************/
            fileWriter.print("digraph structs {\n");
            fileWriter.print("rankdir = LR\n");
            fileWriter.print("node [shape=record];\n");

            /*******************************/
            /* [3] Write Hash Table Itself */
            /*******************************/
            fileWriter.print("hashTable [label=\"");
            for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
            fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);

            /****************************************************************************/
            /* [4] Loop over hash table array and print all linked lists per array cell */
            /****************************************************************************/
            for (i=0;i<hashArraySize;i++)
            {
                if (table[i] != null)
                {
                    /*****************************************************/
                    /* [4a] Print hash table array[i] -> entry(i,0) edge */
                    /*****************************************************/
                    fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
                }
                j=0;
                for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
                {
                    /*******************************/
                    /* [4b] Print entry(i,it) node */
                    /*******************************/
                    fileWriter.format("node_%d_%d ",i,j);
                    fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
                            it.name,
                            it.type.name,
                            it.prevtop_index);

                    if (it.next != null)
                    {
                        /***************************************************/
                        /* [4c] Print entry(i,it) -> entry(i,it.next) edge */
                        /***************************************************/
                        fileWriter.format(
                                "node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
                                i,j,i,j+1);
                        fileWriter.format(
                                "node_%d_%d:f3 -> node_%d_%d:f0;\n",
                                i,j,i,j+1);
                    }
                    j++;
                }
            }
            fileWriter.print("}\n");
            fileWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static SYMBOL_TABLE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected SYMBOL_TABLE() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static SYMBOL_TABLE getInstance()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new SYMBOL_TABLE();

//            instance.beginScope("global");

            /*****************************************/
            /* [1] Enter primitive types int, string */
            /*****************************************/
            instance.enter("int",   TYPE_INT.getInstance(), 0);
            instance.enter("string",TYPE_STRING.getInstance(), 0);

            /*************************************/
            /* [2] How should we handle void ??? */
            /*************************************/

            instance.enter("void",TYPE_VOID.getInstance(), 0);
            instance.enter("nil",TYPE_NIL.getInstance(), 0);

            /***************************************/
            /* [3] Enter library function PrintInt */
            /***************************************/
            instance.enter(
                    "PrintInt",
                    new TYPE_FUNCTION(
                            TYPE_VOID.getInstance(),
                            "PrintInt",
                            new TYPE_LIST(
                                    TYPE_INT.getInstance(),
                                    null)), 0);
            instance.enter(
                    "PrintString",
                    new TYPE_FUNCTION(
                            TYPE_VOID.getInstance(),
                            "PrintString",
                            new TYPE_LIST(
                                    TYPE_STRING.getInstance(),
                                    null)), 0);

            instance.enter(
                    "PrintTrace",
                    new TYPE_FUNCTION(
                            TYPE_VOID.getInstance(),
                            "PrintTrace",
                            new TYPE_LIST(
                                    null,
                                    null)), 0);
        }
        return instance;
    }
}