
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
import java.util.HashMap;
public class CWE197_Numeric_Truncation_Error__int_listen_tcp_to_short_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        {
            IO.writeLine((short)data);
        }
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap ) throws Throwable
    {
        int data = dataHashMap.get(2);
        {
            IO.writeLine((short)data);
        }
    }
}
