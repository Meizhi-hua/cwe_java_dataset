
package testcases.CWE190_Integer_Overflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_max_add_68a extends AbstractTestCase
{
    public static int data;
    public void bad() throws Throwable
    {
        data = Integer.MAX_VALUE;
        (new CWE190_Integer_Overflow__int_max_add_68b()).badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        data = 2;
        (new CWE190_Integer_Overflow__int_max_add_68b()).goodG2BSink();
    }
    private void goodB2G() throws Throwable
    {
        data = Integer.MAX_VALUE;
        (new CWE190_Integer_Overflow__int_max_add_68b()).goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
