
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__StringBuilder_81_goodG2B extends CWE476_NULL_Pointer_Dereference__StringBuilder_81_base
{
    public void action(StringBuilder data ) throws Throwable
    {
        IO.writeLine("" + data.length());
    }
}
