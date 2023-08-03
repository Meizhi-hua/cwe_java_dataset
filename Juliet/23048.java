
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import java.util.LinkedList;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_rand_square_73b
{
    public void badSink(LinkedList<Byte> dataLinkedList ) throws Throwable
    {
        byte data = dataLinkedList.remove(2);
        byte result = (byte)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodG2BSink(LinkedList<Byte> dataLinkedList ) throws Throwable
    {
        byte data = dataLinkedList.remove(2);
        byte result = (byte)(data * data);
        IO.writeLine("result: " + result);
    }
    public void goodB2GSink(LinkedList<Byte> dataLinkedList ) throws Throwable
    {
        byte data = dataLinkedList.remove(2);
        if ((data != Integer.MIN_VALUE) && (data != Long.MIN_VALUE) && (Math.abs(data) <= (long)Math.sqrt(Byte.MAX_VALUE)))
        {
            byte result = (byte)(data * data);
            IO.writeLine("result: " + result);
        }
        else
        {
            IO.writeLine("data value is too large to perform squaring.");
        }
    }
}
