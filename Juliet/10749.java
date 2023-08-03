
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_for_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        if (privateFive == 5)
        {
            for (int i = 0; i < 10; i++)
            {
            }
            IO.writeLine("Hello from bad()");
        }
    }
    private void good1() throws Throwable
    {
        if (privateFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            for (int i = 0; i < 10; i++)
            {
                IO.writeLine("Inside the for statement");
            }
            IO.writeLine("Hello from good()");
        }
    }
    private void good2() throws Throwable
    {
        if (privateFive == 5)
        {
            for (int i = 0; i < 10; i++)
            {
                IO.writeLine("Inside the for statement");
            }
            IO.writeLine("Hello from good()");
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
