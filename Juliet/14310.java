
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        HashMap intHashMap = new HashMap(data);
    }
}
