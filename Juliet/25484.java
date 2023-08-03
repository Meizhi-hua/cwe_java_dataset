
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_max_add_74b
{
    public void badSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
        short result = (short)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HashMap<Integer,Short> dataHashMap ) throws Throwable
    {
        short data = dataHashMap.get(2);
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
