
package testcases.CWE601_Open_Redirect;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.net.URI;
import java.net.URISyntaxException;
public class CWE601_Open_Redirect__Servlet_getQueryString_Servlet_42 extends AbstractTestCaseServlet
{
    private String badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
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
        return data;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = badSource(request, response);
        if (data != null)
        {
            URI uri;
            try
            {
                uri = new URI(data);
            }
            catch (URISyntaxException exceptURISyntax)
            {
                response.getWriter().write("Invalid redirect URL");
                return;
            }
            response.sendRedirect(data);
            return;
        }
    }
    private String goodG2BSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        return data;
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = goodG2BSource(request, response);
        if (data != null)
        {
            URI uri;
            try
            {
                uri = new URI(data);
            }
            catch (URISyntaxException exceptURISyntax)
            {
                response.getWriter().write("Invalid redirect URL");
                return;
            }
            response.sendRedirect(data);
            return;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
