
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashMap_13 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE == 5)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE != 5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        HashMap intHashMap = new HashMap(data);
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE == 5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        HashMap intHashMap = new HashMap(data);
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
