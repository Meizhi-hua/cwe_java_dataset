
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_ArrayList_66b
{
    public void badSink(int dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataArray[2];
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(int dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataArray[2];
        ArrayList intArrayList = new ArrayList(data);
    }
}
