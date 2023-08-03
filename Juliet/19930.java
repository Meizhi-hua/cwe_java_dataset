
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_database_53d
{
    public void badSink(int count ) throws Throwable
    {
        Thread.sleep(count);
    }
    public void goodG2BSink(int count ) throws Throwable
    {
        Thread.sleep(count);
    }
    public void goodB2GSink(int count ) throws Throwable
    {
        if (count > 0 && count <= 2000)
        {
            Thread.sleep(count);
        }
    }
}
