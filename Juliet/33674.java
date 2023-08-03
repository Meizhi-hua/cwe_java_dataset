
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.util.HashMap;
import java.util.logging.Level;
public class CWE400_Resource_Exhaustion__sleep_Property_74a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        count = Integer.MIN_VALUE; 
        {
            String stringNumber = System.getProperty("user.home");
            try
            {
                count = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception parsing count from string", exceptNumberFormat);
            }
        }
        HashMap<Integer,Integer> countHashMap = new HashMap<Integer,Integer>();
        countHashMap.put(0, count);
        countHashMap.put(1, count);
        countHashMap.put(2, count);
        (new CWE400_Resource_Exhaustion__sleep_Property_74b()).badSink(countHashMap  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int count;
        count = 2;
        HashMap<Integer,Integer> countHashMap = new HashMap<Integer,Integer>();
        countHashMap.put(0, count);
        countHashMap.put(1, count);
        countHashMap.put(2, count);
        (new CWE400_Resource_Exhaustion__sleep_Property_74b()).goodG2BSink(countHashMap  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = Integer.MIN_VALUE; 
        {
            String stringNumber = System.getProperty("user.home");
            try
            {
                count = Integer.parseInt(stringNumber.trim());
            }
            catch(NumberFormatException exceptNumberFormat)
            {
                IO.logger.log(Level.WARNING, "Number format exception parsing count from string", exceptNumberFormat);
            }
        }
        HashMap<Integer,Integer> countHashMap = new HashMap<Integer,Integer>();
        countHashMap.put(0, count);
        countHashMap.put(1, count);
        countHashMap.put(2, count);
        (new CWE400_Resource_Exhaustion__sleep_Property_74b()).goodB2GSink(countHashMap  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
