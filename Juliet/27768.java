
package testcases.CWE80_XSS.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_PropertiesFile_81_goodG2B extends CWE80_XSS__Servlet_PropertiesFile_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
