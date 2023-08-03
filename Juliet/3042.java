
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_console_readLine_divide_81_goodB2G extends CWE369_Divide_by_Zero__float_console_readLine_divide_81_base
{
    public void action(float data ) throws Throwable
    {
        if (Math.abs(data) > 0.000001)
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
}
