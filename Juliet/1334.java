
package testcases.CWE89_SQL_Injection.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__getParameter_Servlet_executeQuery_71a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        (new CWE89_SQL_Injection__getParameter_Servlet_executeQuery_71b()).badSink((Object)data , request, response );
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
        (new CWE89_SQL_Injection__getParameter_Servlet_executeQuery_71b()).goodG2BSink((Object)data , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        (new CWE89_SQL_Injection__getParameter_Servlet_executeQuery_71b()).goodB2GSink((Object)data , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
