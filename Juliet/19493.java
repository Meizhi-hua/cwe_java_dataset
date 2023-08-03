
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__getParameter_Servlet_for_loop_71b
{
    public void badSink(Object countObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = (Integer)countObject;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodG2BSink(Object countObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = (Integer)countObject;
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodB2GSink(Object countObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count = (Integer)countObject;
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
