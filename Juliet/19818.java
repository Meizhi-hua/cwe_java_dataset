
package testcases.CWE129_Improper_Validation_of_Array_Index.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_74a extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_74b()).badSink(dataHashMap , request, response );
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data;
        data = 2;
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_74b()).goodG2BSink(dataHashMap , request, response );
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
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
        HashMap<Integer,Integer> dataHashMap = new HashMap<Integer,Integer>();
        dataHashMap.put(0, data);
        dataHashMap.put(1, data);
        dataHashMap.put(2, data);
        (new CWE129_Improper_Validation_of_Array_Index__getCookies_Servlet_array_write_no_check_74b()).goodB2GSink(dataHashMap , request, response );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
