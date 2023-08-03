
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_connect_tcp_multiply_53d
{
    public void badSink(int data ) throws Throwable
    {
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        if(data > 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void goodB2GSink(int data ) throws Throwable
    {
        if(data > 0) 
        {
            if (data < (Integer.MAX_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too large to perform multiplication.");
            }
        }
    }
}
