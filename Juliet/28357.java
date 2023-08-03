
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__getCookies_Servlet_addCookieServlet_68b
{
    public void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE113_HTTP_Response_Splitting__getCookies_Servlet_addCookieServlet_68a.data;
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
    public void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE113_HTTP_Response_Splitting__getCookies_Servlet_addCookieServlet_68a.data;
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
    public void goodB2GSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = CWE113_HTTP_Response_Splitting__getCookies_Servlet_addCookieServlet_68a.data;
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", URLEncoder.encode(data, "UTF-8"));
            response.addCookie(cookieSink);
        }
    }
}
