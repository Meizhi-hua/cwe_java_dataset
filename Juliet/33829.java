
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_ArrayList_71b
{
    public void badSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (Integer)dataObject;
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = (Integer)dataObject;
        ArrayList intArrayList = new ArrayList(data);
    }
}
