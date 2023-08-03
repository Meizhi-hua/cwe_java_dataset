
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
public class CWE197_Numeric_Truncation_Error__int_large_to_byte_41 extends AbstractTestCase
{
    private void badSink(int data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
    public void bad() throws Throwable
    {
        int data;
        data = Short.MAX_VALUE + 5;
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2BSink(int data ) throws Throwable
    {
        {
            IO.writeLine((byte)data);
        }
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        goodG2BSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
