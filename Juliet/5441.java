
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__int_random_to_byte_17 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        for (int i = 0; i < 1; i++)
        {
            {
                IO.writeLine((byte)data);
            }
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        for (int i = 0; i < 1; i++)
        {
            {
                IO.writeLine((byte)data);
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
