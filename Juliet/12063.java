
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Property_68a extends AbstractTestCase
{
    public static String data;
    public void bad() throws Throwable
    {
        data = System.getProperty("user.home");
        (new CWE470_Unsafe_Reflection__Property_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        data = "Testing.test";
        (new CWE470_Unsafe_Reflection__Property_68b()).goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
