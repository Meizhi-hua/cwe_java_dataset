
package testcases.CWE190_Integer_Overflow.s02;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_getQueryString_Servlet_multiply_73b
{
    public void badSink(LinkedList<Integer> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(LinkedList<Integer> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(LinkedList<Integer> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataLinkedList.remove(2);
        if(data > 0) 
        {
            if (data < (Integer.MAX_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
}
