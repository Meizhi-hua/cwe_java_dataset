
package testcases.CWE613_Insufficient_Session_Expiration;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE613_Insufficient_Session_Expiration__Servlet_01 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        HttpSession sesssion = request.getSession(true);
        sesssion.setMaxInactiveInterval(-1);
        response.getWriter().write("bad(): Session still valid");
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        good1(request, response);
    }
    private void good1(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        HttpSession sesssion = request.getSession(true);
        if (sesssion.getCreationTime() + 30000 > System.currentTimeMillis())
        {
            response.getWriter().write("good(): Invalidating session per absolute timeout enforcement");
            sesssion.invalidate();
            return;
        }
        else
        {
            response.getWriter().write("good(): Session still valid");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
