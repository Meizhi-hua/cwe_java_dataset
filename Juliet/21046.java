
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE369_Divide_by_Zero__int_Property_divide_72a extends AbstractTestCase
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
        Vector<Integer> dataVector = new Vector<Integer>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE369_Divide_by_Zero__int_Property_divide_72b()).badSink(dataVector  );
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
        Vector<Integer> dataVector = new Vector<Integer>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE369_Divide_by_Zero__int_Property_divide_72b()).goodG2BSink(dataVector  );
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
        Vector<Integer> dataVector = new Vector<Integer>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE369_Divide_by_Zero__int_Property_divide_72b()).goodB2GSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
