
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_long_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        long data;
        data = 5L;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            ; 
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        data = 5L;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            IO.writeLine("" + data);
        }
    }
    private void goodB2G2() throws Throwable
    {
        long data;
        data = 5L;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            IO.writeLine("" + data);
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
