
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_68a extends AbstractTestCase
{
    public static int count;
    public void bad() throws Throwable
    {
        count = (new SecureRandom()).nextInt();
        (new CWE400_Resource_Exhaustion__sleep_random_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        count = 2;
        (new CWE400_Resource_Exhaustion__sleep_random_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        count = (new SecureRandom()).nextInt();
        (new CWE400_Resource_Exhaustion__sleep_random_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
