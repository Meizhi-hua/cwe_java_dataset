
package testcases.CWE113_HTTP_Response_Splitting.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__Property_setHeaderServlet_81_bad extends CWE113_HTTP_Response_Splitting__Property_setHeaderServlet_81_base
{
    public void action(String data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (data != null)
        {
            response.setHeader("Location", "/author.jsp?lang=" + data);
        }
    }
}
