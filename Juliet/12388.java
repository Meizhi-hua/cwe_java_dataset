
package testcases.CWE197_Numeric_Truncation_Error.s02;
import testcasesupport.*;
import java.util.Vector;
public class CWE197_Numeric_Truncation_Error__int_URLConnection_to_byte_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        {
            IO.writeLine((byte)data);
        }
    }
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        {
            IO.writeLine((byte)data);
        }
    }
}
