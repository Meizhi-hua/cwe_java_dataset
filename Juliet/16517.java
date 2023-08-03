
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE690_NULL_Deref_From_Return__System_getProperty_equals_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if(data.equals("CWE690"))
        {
            IO.writeLine("data is CWE690");
        }
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        String data = (String)dataObject;
        if("CWE690".equals(data))
        {
            IO.writeLine("data is CWE690");
        }
    }
}
