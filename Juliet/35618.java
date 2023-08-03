
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_multiply_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        long data = (Long)dataObject;
        if(data < 0) 
        {
            if (data > (Long.MIN_VALUE/2))
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
