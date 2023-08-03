
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.security.SecureRandom;
public class CWE197_Numeric_Truncation_Error__int_random_to_byte_67a extends AbstractTestCase
{
    static class Container
    {
        public int containerOne;
    }
    public void bad() throws Throwable
    {
        int data;
        data = (new SecureRandom()).nextInt();
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE197_Numeric_Truncation_Error__int_random_to_byte_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE197_Numeric_Truncation_Error__int_random_to_byte_67b()).goodG2BSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
