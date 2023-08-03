
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_max_add_74b
{
    public void badSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        byte result = (byte)(data + 1);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(HashMap<Integer,Byte> dataHashMap ) throws Throwable
    {
        byte data = dataHashMap.get(2);
        if (data < Byte.MAX_VALUE)
        {
            byte result = (byte)(data + 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform addition.");
        }
    }
}
