
package testcases.CWE191_Integer_Underflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
public class CWE191_Integer_Underflow__int_Environment_multiply_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            String stringNumber = System.getenv("ADD");
            if (stringNumber != null) 
            {
                try
                {
                    data = Integer.parseInt(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        return data;
    }
    public void bad() throws Throwable
    {
        int data = badSource();
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private int goodG2BSource() throws Throwable
    {
        int data;
        data = 2;
        return data;
    }
    private void goodG2B() throws Throwable
    {
        int data = goodG2BSource();
        if(data < 0) 
        {
            int result = (int)(data * 2);
            IO.writeLine("result: " + result);
        }
    }
    private int goodB2GSource() throws Throwable
    {
        int data;
        data = Integer.MIN_VALUE; 
        {
            String stringNumber = System.getenv("ADD");
            if (stringNumber != null) 
            {
                try
                {
                    data = Integer.parseInt(stringNumber.trim());
                }
                catch(NumberFormatException exceptNumberFormat)
                {
                    IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                }
            }
        }
        return data;
    }
    private void goodB2G() throws Throwable
    {
        int data = goodB2GSource();
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
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
