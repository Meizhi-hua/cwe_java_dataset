
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_modulo_16 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        while (true)
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
            break;
        }
        while (true)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
            break;
        }
    }
    private void goodG2B() throws Throwable
    {
        float data;
        while (true)
        {
            data = 2.0f;
            break;
        }
        while (true)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
            break;
        }
    }
    private void goodB2G() throws Throwable
    {
        float data;
        while (true)
        {
            SecureRandom secureRandom = new SecureRandom();
            data = secureRandom.nextFloat();
            break;
        }
        while (true)
        {
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 % data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
            break;
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
