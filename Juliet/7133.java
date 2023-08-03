
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__Property_ArrayList_06 extends AbstractTestCase
{
    private static final int PRIVATE_STATIC_FINAL_FIVE = 5;
    public void bad() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
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
        }
        else
        {
            data = 0;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE != 5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        ArrayList intArrayList = new ArrayList(data);
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
