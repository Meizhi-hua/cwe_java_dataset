
package testcases.CWE134_Uncontrolled_Format_String.s02;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__URLConnection_format_81_goodB2G extends CWE134_Uncontrolled_Format_String__URLConnection_format_81_base
{
    public void action(String data ) throws Throwable
    {
        if (data != null)
        {
            System.out.format("%s%n", data);
        }
    }
}
