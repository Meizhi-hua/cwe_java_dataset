
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_database_divide_81_bad extends CWE369_Divide_by_Zero__float_database_divide_81_base
{
    public void action(float data ) throws Throwable
    {
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
}
