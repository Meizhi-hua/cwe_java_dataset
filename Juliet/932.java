
package testcases.CWE80_XSS.s01;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE80_XSS__CWE182_Servlet_getQueryString_Servlet_73b
{
    public void badSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data.replaceAll("(<script>)", ""));
        }
    }
    public void goodG2BSink(LinkedList<String> dataLinkedList , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataLinkedList.remove(2);
        if (data != null)
        {
            response.getWriter().println("<br>bad(): data = " + data.replaceAll("(<script>)", ""));
        }
    }
}
