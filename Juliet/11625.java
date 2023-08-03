
package testcases.CWE400_Resource_Exhaustion.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__getQueryString_Servlet_for_loop_16 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        while (true)
        {
            count = Integer.MIN_VALUE; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        try
                        {
                            count = Integer.parseInt(token.substring(3)); 
                        }
                        catch(NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                        }
                        break; 
                    }
                }
            }
            break;
        }
        while (true)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
            break;
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        while (true)
        {
            count = 2;
            break;
        }
        while (true)
        {
            int i = 0;
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
            break;
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int count;
        while (true)
        {
            count = Integer.MIN_VALUE; 
            {
                StringTokenizer tokenizer = new StringTokenizer(request.getQueryString(), "&");
                while (tokenizer.hasMoreTokens())
                {
                    String token = tokenizer.nextToken(); 
                    if(token.startsWith("id=")) 
                    {
                        try
                        {
                            count = Integer.parseInt(token.substring(3)); 
                        }
                        catch(NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception reading id from query string", exceptNumberFormat);
                        }
                        break; 
                    }
                }
            }
            break;
        }
        while (true)
        {
            int i = 0;
            if (count > 0 && count <= 20)
            {
                for (i = 0; i < count; i++)
                {
                    IO.writeLine("Hello");
                }
            }
            break;
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
