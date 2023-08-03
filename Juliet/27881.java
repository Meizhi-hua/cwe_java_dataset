
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_console_readLine_multiply_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        short data = (Short)dataObject;
        if(data < 0) 
        {
            short result = (short)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        short data = (Short)dataObject;
        if(data < 0) 
        {
            short result = (short)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        short data = (Short)dataObject;
        if(data < 0) 
        {
            if (data > (Short.MIN_VALUE/2))
            {
                short result = (short)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
