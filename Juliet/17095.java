
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_uninit_variable_StringBuilder_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        ; 
        if (IO.staticFive == 5)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data;
        ; 
        if (IO.staticFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G2() throws Throwable
    {
        StringBuilder data;
        ; 
        if (IO.staticFive == 5)
        {
            data = new StringBuilder("Good");
            IO.writeLine(data.toString());
        }
    }
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
