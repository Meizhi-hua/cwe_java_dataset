
package testcases.CWE400_Resource_Exhaustion.s03;
import testcasesupport.*;
public class CWE400_Resource_Exhaustion__sleep_max_value_11 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int count;
        if (IO.staticReturnsTrue())
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if(IO.staticReturnsTrue())
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int count;
        if (IO.staticReturnsFalse())
        {
            count = 0;
        }
        else
        {
            count = 2;
        }
        if (IO.staticReturnsTrue())
        {
            Thread.sleep(count);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int count;
        if (IO.staticReturnsTrue())
        {
            count = 2;
        }
        else
        {
            count = 0;
        }
        if (IO.staticReturnsTrue())
        {
            Thread.sleep(count);
        }
    }
    private void goodB2G1() throws Throwable
    {
        int count;
        if (IO.staticReturnsTrue())
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (IO.staticReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int count;
        if (IO.staticReturnsTrue())
        {
            count = Integer.MAX_VALUE;
        }
        else
        {
            count = 0;
        }
        if (IO.staticReturnsTrue())
        {
            if (count > 0 && count <= 2000)
            {
                Thread.sleep(count);
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
