
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__short_large_42 extends AbstractTestCase
{
    private short badSource() throws Throwable
    {
        short data;
        data = Byte.MAX_VALUE + 5;
        return data;
    }
    public void bad() throws Throwable
    {
        short data = badSource();
        {
            IO.writeLine((byte)data);
        }
    }
    private short goodG2BSource() throws Throwable
    {
        short data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        short data = goodG2BSource();
        {
            IO.writeLine((byte)data);
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
