
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__float_random_divide_52a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        (new CWE369_Divide_by_Zero__float_random_divide_52b()).badSink(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        (new CWE369_Divide_by_Zero__float_random_divide_52b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        SecureRandom secureRandom = new SecureRandom();
        data = secureRandom.nextFloat();
        (new CWE369_Divide_by_Zero__float_random_divide_52b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
