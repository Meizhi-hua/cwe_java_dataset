
package testcases.CWE113_HTTP_Response_Splitting.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__Property_addCookieServlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", URLEncoder.encode(data, "UTF-8"));
            response.addCookie(cookieSink);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
