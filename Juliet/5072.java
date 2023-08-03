
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__getParameter_Servlet_trim_74b
{
    public void badSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodG2BSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void goodB2GSink(HashMap<Integer,String> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataHashMap.get(2);
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
}
