
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__getQueryString_Servlet_for_loop_67b
{
    public void badSink(CWE400_Resource_Exhaustion__getQueryString_Servlet_for_loop_67a.Container countContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countContainer.containerOne;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodG2BSink(CWE400_Resource_Exhaustion__getQueryString_Servlet_for_loop_67a.Container countContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countContainer.containerOne;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodB2GSink(CWE400_Resource_Exhaustion__getQueryString_Servlet_for_loop_67a.Container countContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = countContainer.containerOne;
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
}
