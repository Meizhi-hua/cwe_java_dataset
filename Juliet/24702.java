
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_rand_sub_02 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        short data;
        if (true)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        short data;
        if (false)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (true)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        short data;
        if (true)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            short result = (short)(data - 1);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        short data;
        if (true)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (false)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data > Short.MIN_VALUE)
            {
                short result = (short)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        short data;
        if (true)
        {
            data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        }
        else
        {
            data = 0;
        }
        if (true)
        {
            if (data > Short.MIN_VALUE)
            {
                short result = (short)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
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
