
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__URLConnection_for_loop_81_goodB2G extends CWE400_Resource_Exhaustion__URLConnection_for_loop_81_base
{
    public void action(int count ) throws Throwable
    {
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
}
