
package testcases.CWE129_Improper_Validation_of_Array_Index.s05;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__URLConnection_array_size_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        int array[] = null;
        if (data >= 0)
        {
            array = new int[data];
        }
        else
        {
            IO.writeLine("Array size is negative");
        }
        array[0] = 5;
        IO.writeLine(array[0]);
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        int array[] = null;
        if (data >= 0)
        {
            array = new int[data];
        }
        else
        {
            IO.writeLine("Array size is negative");
        }
        array[0] = 5;
        IO.writeLine(array[0]);
    }
    public void goodB2GSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        int array[] = null;
        if (data > 0)
        {
            array = new int[data];
        }
        else
        {
            IO.writeLine("Array size is negative");
        }
        array[0] = 5;
        IO.writeLine(array[0]);
    }
}
