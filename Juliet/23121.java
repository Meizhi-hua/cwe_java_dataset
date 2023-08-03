
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_getCookies_Servlet_modulo_31 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = Integer.MIN_VALUE; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    String stringNumber = cookieSources[0].getValue();
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading data from cookie", exceptNumberFormat);
                    }
                }
            }
            dataCopy = data;
        }
        {
            int data = dataCopy;
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = 2;
            dataCopy = data;
        }
        {
            int data = dataCopy;
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int dataCopy;
        {
            int data;
            data = Integer.MIN_VALUE; 
            {
                Cookie cookieSources[] = request.getCookies();
                if (cookieSources != null)
                {
                    String stringNumber = cookieSources[0].getValue();
                    try
                    {
                        data = Integer.parseInt(stringNumber.trim());
                    }
                    catch(NumberFormatException exceptNumberFormat)
                    {
                        IO.logger.log(Level.WARNING, "Number format exception reading data from cookie", exceptNumberFormat);
                    }
                }
            }
            dataCopy = data;
        }
        {
            int data = dataCopy;
            if (data != 0)
            {
                IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
