
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__long_min_multiply_08 extends AbstractTestCase
{
    private boolean privateReturnsTrue()
    {
        return true;
    }
    private boolean privateReturnsFalse()
    {
        return false;
    }
    public void bad() throws Throwable
    {
        long data;
        if (privateReturnsTrue())
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsTrue())
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        long data;
        if (privateReturnsFalse())
        {
            data = 0L;
        }
        else
        {
            data = 2;
        }
        if (privateReturnsTrue())
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        long data;
        if (privateReturnsTrue())
        {
            data = 2;
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsTrue())
        {
            if(data < 0) 
            {
                long result = (long)(data * 2);
                IO.writeLine("result: " + result);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        long data;
        if (privateReturnsTrue())
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsFalse())
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if(data < 0) 
            {
                if (data > (Long.MIN_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        long data;
        if (privateReturnsTrue())
        {
            data = Long.MIN_VALUE;
        }
        else
        {
            data = 0L;
        }
        if (privateReturnsTrue())
        {
            if(data < 0) 
            {
                if (data > (Long.MIN_VALUE/2))
                {
                    long result = (long)(data * 2);
                    IO.writeLine("result: " + result);
                }
                else
                {
                    IO.writeLine("data value is too small to perform multiplication.");
                }
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
