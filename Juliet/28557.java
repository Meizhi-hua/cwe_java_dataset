
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__getParameter_Servlet_81_goodG2B extends CWE470_Unsafe_Reflection__getParameter_Servlet_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
}
