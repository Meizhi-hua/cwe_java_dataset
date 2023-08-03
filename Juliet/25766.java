
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE643_Xpath_Injection__getParameter_Servlet_81a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        CWE643_Xpath_Injection__getParameter_Servlet_81_base baseObject = new CWE643_Xpath_Injection__getParameter_Servlet_81_bad();
        baseObject.action(data , request, response);
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
        CWE643_Xpath_Injection__getParameter_Servlet_81_base baseObject = new CWE643_Xpath_Injection__getParameter_Servlet_81_goodG2B();
        baseObject.action(data , request, response);
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = request.getParameter("name");
        CWE643_Xpath_Injection__getParameter_Servlet_81_base baseObject = new CWE643_Xpath_Injection__getParameter_Servlet_81_goodB2G();
        baseObject.action(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
