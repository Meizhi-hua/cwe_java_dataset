
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_Environment_81_bad extends CWE400_Resource_Exhaustion__sleep_Environment_81_base
{
    public void action(int count ) throws Throwable
    {
        Thread.sleep(count);
    }
}
