
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_HashSet_14 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticFive == 5)
        {
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
        }
        else
        {
            data = 0;
        }
        HashSet intHashSet = new HashSet(data);
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.staticFive != 5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        HashSet intHashSet = new HashSet(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.staticFive == 5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        HashSet intHashSet = new HashSet(data);
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
