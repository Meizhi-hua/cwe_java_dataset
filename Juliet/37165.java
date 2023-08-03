
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_rand_multiply_68b
{
    public void badSink() throws Throwable
    {
        long data = CWE191_Integer_Underflow__long_rand_multiply_68a.data;
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink() throws Throwable
    {
        long data = CWE191_Integer_Underflow__long_rand_multiply_68a.data;
        if(data < 0) 
        {
            long result = (long)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink() throws Throwable
    {
        long data = CWE191_Integer_Underflow__long_rand_multiply_68a.data;
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
