
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE789_Uncontrolled_Mem_Alloc__Property_HashMap_61b
{
    public int badSource() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            String stringNumber = System.getProperty("user.home");
            try
            {
                data = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
            }
        }
        return data;
    }
    public int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
}
