
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_while_03 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        if (5 == 5)
        {
            int i = 0;
            while(i++ < 10)
            {
            }
            IO.writeLine("Hello from bad()");
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
            int i = 0;
            while(i++ < 10)
            {
                IO.writeLine("Inside the while statement");
            }
            IO.writeLine("Hello from good()");
        }
    }
    private void good2() throws Throwable
    {
        if (5 == 5)
        {
            int i = 0;
            while(i++ < 10)
            {
                IO.writeLine("Inside the while statement");
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
