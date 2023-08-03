
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_read_no_check_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
    public void goodB2GSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0 && data < array.length)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
