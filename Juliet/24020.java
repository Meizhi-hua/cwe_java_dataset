
package testcases.CWE511_Logic_Time_Bomb;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE511_Logic_Time_Bomb__rand_06 extends AbstractTestCase
{
    private static final int PRIVATE_STATIC_FINAL_FIVE = 5;
    public void bad() throws Throwable
    {
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
            if ((new SecureRandom()).nextInt() == 20000)
            {
                Runtime.getRuntime().exec("c:\\windows\\system32\\evil.exe");
            }
        }
    }
    private void good1() throws Throwable
    {
        if (PRIVATE_STATIC_FINAL_FIVE != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if ((new SecureRandom()).nextInt() == 20000)
            {
                IO.writeLine("Sorry, your license has expired.  Please contact support.");
            }
        }
    }
    private void good2() throws Throwable
    {
        if (PRIVATE_STATIC_FINAL_FIVE == 5)
        {
            if ((new SecureRandom()).nextInt() == 20000)
            {
                IO.writeLine("Sorry, your license has expired.  Please contact support.");
            }
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
