
package testcases.CWE617_Reachable_Assertion;
import testcasesupport.*;
public class CWE617_Reachable_Assertion__length_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (5 == 5)
        {
            assert "".length() > 0;
        }
    }
    private void good1() throws Throwable
    {
        if (5 != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            assert "cwe617".length() > 0;
        }
    }
    private void good2() throws Throwable
    {
        if (5 == 5)
        {
            assert "cwe617".length() > 0;
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
