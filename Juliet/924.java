
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_67a extends AbstractTestCaseServlet
{
    static class Container
    {
        public int containerOne;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
            while (tokenizer.hasMoreTokens())
            {
                String token = tokenizer.nextToken(); 
                if(token.startsWith("id=")) 
                {
                    try
                    {
                        data = Integer.parseInt(token.substring(3)); 
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                    }
                    break; 
                }
            }
        }
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_67b()).badSink(dataContainer , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashSet_67b()).goodG2BSink(dataContainer , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
