
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE369_Divide_by_Zero__int_Property_divide_71b
{
    public void badSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodG2BSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
    }
    public void goodB2GSink(Object dataObject ) throws Throwable
    {
        int data = (Integer)dataObject;
        if (data != 0)
        {
            IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
        }
        else
        {
            IO.writeLine("This would result in a divide by zero");
        }
    }
}
