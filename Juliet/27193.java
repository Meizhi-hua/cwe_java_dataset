
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_divide_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        float data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = 0.0f; 
        }
        else
        {
            data = 0.0f;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodG2B1() throws Throwable
    {
        float data;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            data = 0.0f;
        }
        else
        {
            data = 2.0f;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodG2B2() throws Throwable
    {
        float data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = 2.0f;
        }
        else
        {
            data = 0.0f;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            int result = (int)(100.0 / data);
            IO.writeLine(result);
        }
    }
    private void goodB2G1() throws Throwable
    {
        float data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = 0.0f; 
        }
        else
        {
            data = 0.0f;
        }
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 / data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        float data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = 0.0f; 
        }
        else
        {
            data = 0.0f;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (Math.abs(data) > 0.000001)
            {
                int result = (int)(100.0 / data);
                IO.writeLine(result);
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
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
