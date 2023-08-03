
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
import java.util.Vector;
public class CWE476_NULL_Pointer_Dereference__Integer_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        Integer data = dataVector.remove(2);
        IO.writeLine("" + data.toString());
    }
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        Integer data = dataVector.remove(2);
        IO.writeLine("" + data.toString());
    }
    public void goodB2GSink(Vector<Integer> dataVector ) throws Throwable
    {
        Integer data = dataVector.remove(2);
        if (data != null)
        {
            IO.writeLine("" + data.toString());
        }
        else
        {
            IO.writeLine("data is null");
        }
    }
}
