
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__connect_tcp_addCookieServlet_71b
{
    public void badSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
    public void goodG2BSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", data);
            response.addCookie(cookieSink);
        }
    }
    public void goodB2GSink(Object dataObject , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = (String)dataObject;
        if (data != null)
        {
            Cookie cookieSink = new Cookie("lang", URLEncoder.encode(data, "UTF-8"));
            response.addCookie(cookieSink);
        }
    }
}
