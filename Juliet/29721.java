
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE113_HTTP_Response_Splitting__Environment_addHeaderServlet_68a extends AbstractTestCaseServlet
{
    public static String data;
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE113_HTTP_Response_Splitting__Environment_addHeaderServlet_68b()).badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = "foo";
        (new CWE113_HTTP_Response_Splitting__Environment_addHeaderServlet_68b()).goodG2BSink(request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        data = System.getenv("ADD");
        (new CWE113_HTTP_Response_Splitting__Environment_addHeaderServlet_68b()).goodB2GSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
