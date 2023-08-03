
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__String_05 extends AbstractTestCase
{
    private boolean privateTrue = true;
    private boolean privateFalse = false;
    public void bad() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = null;
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            IO.writeLine("" + data.length());
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (privateFalse)
        {
            data = null;
        }
        else
        {
            data = "This is not null";
        }
        if (privateTrue)
        {
            IO.writeLine("" + data.length());
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = "This is not null";
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            IO.writeLine("" + data.length());
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = null;
        }
        else
        {
            data = null;
        }
        if (privateFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != null)
            {
                IO.writeLine("" + data.length());
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (privateTrue)
        {
            data = null;
        }
        else
        {
            data = null;
        }
        if (privateTrue)
        {
            if (data != null)
            {
                IO.writeLine("" + data.length());
            }
            else
            {
                IO.writeLine("data is null");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
