
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_ArrayList_13 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.STATIC_FINAL_FIVE == 5)
        {
            data = Integer.MAX_VALUE;
        }
        else
        {
            data = 0;
        }
        ArrayList intArrayList = new ArrayList(data);
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
        ArrayList intArrayList = new ArrayList(data);
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
        ArrayList intArrayList = new ArrayList(data);
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
