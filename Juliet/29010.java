
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_53d
{
    public void badSink(short data ) throws Throwable
    {
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(short data ) throws Throwable
    {
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(short data ) throws Throwable
    {
        if (data < Short.MAX_VALUE)
        {
            short result = (short)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
