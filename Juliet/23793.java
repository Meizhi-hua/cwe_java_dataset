
package testcases.CWE477_Obsolete_Functions;
import testcasesupport.*;
import java.net.URLEncoder;
import javax.servlet.http.*;
public class CWE477_Obsolete_Functions__URLEncoder_encode_Servlet_12 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()"));
        }
        else
        {
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()", "UTF-8"));
        }
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (IO.staticReturnsTrueOrFalse())
        {
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()", "UTF-8"));
        }
        else
        {
            response.getWriter().println(URLEncoder.encode("abc|1 $#@<><()", "UTF-8"));
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
