
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Environment_modulo_67b
{
    public void badSink(CWE369_Divide_by_Zero__int_Environment_modulo_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
    }
    public void goodG2BSink(CWE369_Divide_by_Zero__int_Environment_modulo_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
    }
    public void goodB2GSink(CWE369_Divide_by_Zero__int_Environment_modulo_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        if (data != 0)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a modulo by zero");
        }
    }
}
