
package testcases.CWE369_Divide_by_Zero.s01;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_PropertiesFile_divide_67b
{
    public void badSink(CWE369_Divide_by_Zero__float_PropertiesFile_divide_67a.Container dataContainer ) throws Throwable
    {
        float data = dataContainer.containerOne;
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodG2BSink(CWE369_Divide_by_Zero__float_PropertiesFile_divide_67a.Container dataContainer ) throws Throwable
    {
        float data = dataContainer.containerOne;
        int result = (int)(100.0 / data);
        IO.writeLine(result);
    }
    public void goodB2GSink(CWE369_Divide_by_Zero__float_PropertiesFile_divide_67a.Container dataContainer ) throws Throwable
    {
        float data = dataContainer.containerOne;
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
