
package testcases.CWE80_XSS.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE80_XSS__Servlet_URLConnection_66b
{
    public void badSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
    public void goodG2BSink(String dataArray[] , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataArray[2];
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data);
        }
    }
}
