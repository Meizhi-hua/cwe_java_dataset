
package testcases.CWE511_Logic_Time_Bomb;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE511_Logic_Time_Bomb__rand_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if ((new SecureRandom()).nextInt() == 20000)
        {
            Runtime.getRuntime().exec("c:\\windows\\system32\\evil.exe");
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        if ((new SecureRandom()).nextInt() == 20000)
        {
            IO.writeLine("Sorry, your license has expired.  Please contact support.");
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
