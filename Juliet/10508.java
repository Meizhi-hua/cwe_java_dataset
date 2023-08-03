
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_URLConnection_multiply_67b
{
    public void badSink(CWE191_Integer_Underflow__int_URLConnection_multiply_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(CWE191_Integer_Underflow__int_URLConnection_multiply_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(CWE191_Integer_Underflow__int_URLConnection_multiply_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        if(data < 0) 
        {
            if (data > (Integer.MIN_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
}
