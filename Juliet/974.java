
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE400_Resource_Exhaustion__sleep_random_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        CWE400_Resource_Exhaustion__sleep_random_81_base baseObject = new CWE400_Resource_Exhaustion__sleep_random_81_bad();
        baseObject.action(count );
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
        CWE400_Resource_Exhaustion__sleep_random_81_base baseObject = new CWE400_Resource_Exhaustion__sleep_random_81_goodG2B();
        baseObject.action(count );
    }
    private void goodB2G() throws Throwable
    {
        int count;
        count = (new SecureRandom()).nextInt();
        CWE400_Resource_Exhaustion__sleep_random_81_base baseObject = new CWE400_Resource_Exhaustion__sleep_random_81_goodB2G();
        baseObject.action(count );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
