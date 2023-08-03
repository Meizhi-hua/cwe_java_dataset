
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_square_74b
{
    public void badSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        short result = (short)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Short.MAX_VALUE)))
        {
            short result = (short)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
}
