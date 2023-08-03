
package testcases.CWE23_Relative_Path_Traversal;
import testcasesupport.*;
import java.io.*;
import javax.servlet.http.*;
public class CWE23_Relative_Path_Traversal__getCookies_Servlet_81a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            Cookie cookieSources[] = request.getCookies();
            if (cookieSources != null)
            {
                data = cookieSources[0].getValue();
            }
        }
        CWE23_Relative_Path_Traversal__getCookies_Servlet_81_base baseObject = new CWE23_Relative_Path_Traversal__getCookies_Servlet_81_bad();
        baseObject.action(data , request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        CWE23_Relative_Path_Traversal__getCookies_Servlet_81_base baseObject = new CWE23_Relative_Path_Traversal__getCookies_Servlet_81_goodG2B();
        baseObject.action(data , request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
