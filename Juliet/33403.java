
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__getParameter_Servlet_HashSet_42 extends AbstractTestCaseServlet
{
    private int badSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            String stringNumber = request.getParameter("name");
            try
            {
                data = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception reading data from parameter 'name'", exceptNumberFormat);
            }
        }
        return data;
    }
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = badSource(request, response);
        HashSet intHashSet = new HashSet(data);
    }
    private int goodG2BSource(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = goodG2BSource(request, response);
        HashSet intHashSet = new HashSet(data);
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
