
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_URLConnection_modulo_68b
{
    public void badSink() throws Throwable
    {
        float data = CWE369_Divide_by_Zero__float_URLConnection_modulo_68a.data;
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void goodG2BSink() throws Throwable
    {
        float data = CWE369_Divide_by_Zero__float_URLConnection_modulo_68a.data;
        int result = (int)(100.0 % data);
        IO.writeLine(result);
    }
    public void goodB2GSink() throws Throwable
    {
        float data = CWE369_Divide_by_Zero__float_URLConnection_modulo_68a.data;
        if (Math.abs(data) > 0.000001)
        {
            int result = (int)(100.0 % data);
            IO.writeLine(result);
        }
        else
        {
            IO.writeLine("This would result in a modulo by zero");
        }
    }
}
