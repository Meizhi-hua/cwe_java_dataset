
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
public class CWE134_Uncontrolled_Format_String__Property_format_13 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = System.getProperty("user.home");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE!=5)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if (data != null)
            {
                System.out.format(data);
            }
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = System.getProperty("user.home");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE!=5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (IO.STATIC_FINAL_FIVE==5)
        {
            data = System.getProperty("user.home");
        }
        else
        {
            data = null;
        }
        if (IO.STATIC_FINAL_FIVE==5)
        {
            if (data != null)
            {
                System.out.format("%s%n", data);
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
