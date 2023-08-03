
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_81_goodB2G extends CWE476_NULL_Pointer_Dereference__StringBuilder_81_base
{
    public void action(StringBuilder data ) throws Throwable
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
