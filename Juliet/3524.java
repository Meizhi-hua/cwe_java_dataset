
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashSet_53d
{
    public void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        HashSet intHashSet = new HashSet(data);
    }
}
