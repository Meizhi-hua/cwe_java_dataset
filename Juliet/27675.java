
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__getParameter_Servlet_67b
{
    public void badSink(CWE470_Unsafe_Reflection__getParameter_Servlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void goodG2BSink(CWE470_Unsafe_Reflection__getParameter_Servlet_67a.Container dataContainer , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataContainer.containerOne;
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
}
