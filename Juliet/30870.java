
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__getQueryString_Servlet_HashMap_45 extends AbstractTestCaseServlet
{
    private int dataBad;
    private int dataGoodG2B;
    private void badSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataBad;
        HashMap intHashMap = new HashMap(data);
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
        dataBad = data;
        badSink(request, response);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    private void goodG2BSink(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataGoodG2B;
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        dataGoodG2B = data;
        goodG2BSink(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
