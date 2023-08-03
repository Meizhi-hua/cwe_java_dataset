
package testcases.CWE338_Weak_PRNG;
import testcasesupport.*;
import java.security.SecureRandom;
import java.util.Random;
public class CWE338_Weak_PRNG__util_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (5 == 5)
        {
            Random random = new Random();
            IO.writeLine("" + random.nextInt());
        }
    }
    private void good1() throws Throwable
    {
        if (5 != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextDouble());
        }
    }
    private void good2() throws Throwable
    {
        if (5 == 5)
        {
            SecureRandom secureRandom = new SecureRandom();
            IO.writeLine("" + secureRandom.nextDouble());
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
