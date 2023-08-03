
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__max_value_write_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        (new CWE400_Resource_Exhaustion__max_value_write_51b()).badSink(count  );
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
        (new CWE400_Resource_Exhaustion__max_value_write_51b()).goodG2BSink(count  );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = Integer.MAX_VALUE;
        (new CWE400_Resource_Exhaustion__max_value_write_51b()).goodB2GSink(count  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
