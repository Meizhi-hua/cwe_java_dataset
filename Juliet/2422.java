
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_HashSet_67b
{
    public void badSink(CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_HashSet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashSet intHashSet = new HashSet(data);
    }
    public void goodG2BSink(CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_HashSet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataContainer.containerOne;
        HashSet intHashSet = new HashSet(data);
    }
}
