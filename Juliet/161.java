
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__getCookies_Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            Cookie cookieSources[] = request.getCookies();
            if (cookieSources != null)
            {
                data = cookieSources[0].getValue();
            }
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "Testing.test";
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
