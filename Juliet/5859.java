
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_Property_divide_66a extends AbstractTestCase
{
    public void bad() throws Throwable
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
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__int_Property_divide_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__int_Property_divide_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
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
        int[] dataArray = new int[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__int_Property_divide_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
