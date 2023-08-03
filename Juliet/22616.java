
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_StringBuilder_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        if (IO.staticTrue)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine(data.toString());
        }
    }
    private void goodB2G2() throws Throwable
    {
        StringBuilder data;
        data = new StringBuilder("Good");
        if (IO.staticTrue)
        {
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
