
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__getQueryString_Servlet_array_write_no_check_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodB2GSink(LinkedList<Integer> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0 && data < array.length)
        {
            array[data] = 42;
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
