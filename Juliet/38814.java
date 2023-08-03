
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.logging.Level;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__Environment_HashSet_21 extends AbstractTestCase
{
    private boolean badPrivate = false;
    public void bad() throws Throwable
    {
        int data;
        badPrivate = true;
        data = bad_source();
        HashSet intHashSet = new HashSet(data);
    }
    private int bad_source() throws Throwable
    {
        int data;
        if (badPrivate)
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
        return data;
    }
    private boolean goodG2B1_private = false;
    private boolean goodG2B2_private = false;
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        goodG2B1_private = false;
        data = goodG2B1_source();
        HashSet intHashSet = new HashSet(data);
    }
    private int goodG2B1_source() throws Throwable
    {
        int data = 0;
        if (goodG2B1_private)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        return data;
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        goodG2B2_private = true;
        data = goodG2B2_source();
        HashSet intHashSet = new HashSet(data);
    }
    private int goodG2B2_source() throws Throwable
    {
        int data = 0;
        if (goodG2B2_private)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        return data;
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
