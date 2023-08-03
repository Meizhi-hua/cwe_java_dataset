
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
public class CWE191_Integer_Underflow__byte_min_sub_41 extends AbstractTestCase
{
    private void badSink(byte data ) throws Throwable
    {
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    public void bad() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        badSink(data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink(byte data ) throws Throwable
    {
        byte result = (byte)(data - 1);
        IO.writeLine("result: " + result);
    }
    private void goodG2B() throws Throwable
    {
        byte data;
        data = 2;
        goodG2BSink(data  );
    }
    private void goodB2GSink(byte data ) throws Throwable
    {
        if (data > Byte.MIN_VALUE)
        {
            byte result = (byte)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too small to perform subtraction.");
        }
    }
    private void goodB2G() throws Throwable
    {
        byte data;
        data = Byte.MIN_VALUE;
        goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
