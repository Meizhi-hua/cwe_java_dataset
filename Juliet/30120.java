
package testcases.CWE398_Poor_Code_Quality;
import testcasesupport.*;
public class CWE398_Poor_Code_Quality__empty_block_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        if (privateFive == 5)
        {
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
            {
                String sentence = "Inside the block"; 
                IO.writeLine(sentence);
            }
            IO.writeLine("Hello from good()");
        }
    }
    private void good2() throws Throwable
    {
        if (privateFive == 5)
        {
            {
                String sentence = "Inside the block"; 
                IO.writeLine(sentence);
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
