
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_73b
{
    public void badSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
