
package testcases.CWE81_XSS_Error_Message;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
public class CWE81_XSS_Error_Message__Servlet_getQueryString_Servlet_15 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        switch (6)
        {
        case 6:
            data = ""; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        data = token.substring(3); 
                        break; 
                    }
                }
            }
            break;
        default:
            data = null;
            break;
        }
        if (data != null)
        {
            response.sendError(404, "<br>bad() - Parameter name has value " + data);
        }
    }
    private void goodG2B1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        switch (5)
        {
        case 6:
            data = null;
            break;
        default:
            data = "foo";
            break;
        }
        if (data != null)
        {
            response.sendError(404, "<br>bad() - Parameter name has value " + data);
        }
    }
    private void goodG2B2(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = null;
        switch (6)
        {
        case 6:
            data = "foo";
            break;
        default:
            data = null;
            break;
        }
        if (data != null)
        {
            response.sendError(404, "<br>bad() - Parameter name has value " + data);
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B1(request, response);
        goodG2B2(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
