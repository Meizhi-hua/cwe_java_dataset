
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_multiply_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        CWE191_Integer_Underflow__long_min_multiply_81_base baseObject = new CWE191_Integer_Underflow__long_min_multiply_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        long data;
        data = 2;
        CWE191_Integer_Underflow__long_min_multiply_81_base baseObject = new CWE191_Integer_Underflow__long_min_multiply_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = Long.MIN_VALUE;
        CWE191_Integer_Underflow__long_min_multiply_81_base baseObject = new CWE191_Integer_Underflow__long_min_multiply_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
