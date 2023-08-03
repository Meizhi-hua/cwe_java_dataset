
package testcases.CWE601_Open_Redirect;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE601_Open_Redirect__Servlet_getCookies_Servlet_71a extends AbstractTestCaseServlet
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
        (new CWE601_Open_Redirect__Servlet_getCookies_Servlet_71b()).badSink((Object)data , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        (new CWE601_Open_Redirect__Servlet_getCookies_Servlet_71b()).goodG2BSink((Object)data , request, response );
    }
    public static void main(String[] args)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
