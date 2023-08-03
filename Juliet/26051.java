
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_22b
{
    public void badSink(StringBuilder data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__StringBuilder_22a.badPublicStatic)
        {
            IO.writeLine("" + data.length());
        }
        else
        {
            data = null;
        }
    }
    public void goodB2G1Sink(StringBuilder data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__StringBuilder_22a.goodB2G1PublicStatic)
        {
            data = null;
        }
        else
        {
            if (data != null)
            {
                IO.writeLine("" + data.length());
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
    }
    public void goodB2G2Sink(StringBuilder data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__StringBuilder_22a.goodB2G2PublicStatic)
        {
            if (data != null)
            {
                IO.writeLine("" + data.length());
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
        else
        {
            data = null;
        }
    }
    public void goodG2BSink(StringBuilder data ) throws Throwable
    {
        if (CWE476_NULL_Pointer_Dereference__StringBuilder_22a.goodG2BPublicStatic)
        {
            IO.writeLine("" + data.length());
        }
        else
        {
            data = null;
        }
    }
}
