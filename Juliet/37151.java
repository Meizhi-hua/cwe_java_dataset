
package testcases.CWE369_Divide_by_Zero.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_zero_divide_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 0; 
        (new CWE369_Divide_by_Zero__int_zero_divide_71b()).badSink((Object)data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        (new CWE369_Divide_by_Zero__int_zero_divide_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 0; 
        (new CWE369_Divide_by_Zero__int_zero_divide_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
