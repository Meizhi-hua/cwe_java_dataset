
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_81_goodG2B extends CWE690_NULL_Deref_From_Return__Class_String_81_base
{
    public void action(String data ) throws Throwable
    {
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
}
