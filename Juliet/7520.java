
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_min_multiply_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE;
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE;
        if(data < 0) 
        {
            if (data > (Integer.MIN_VALUE/2))
            {
                int result = (int)(data * 2);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform multiplication.");
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
