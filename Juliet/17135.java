
package testcases.CWE789_Uncontrolled_Mem_Alloc.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import java.util.HashSet;
public class CWE789_Uncontrolled_Mem_Alloc__random_HashSet_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticReturnsTrueOrFalse())
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 2;
        }
        HashSet intHashSet = new HashSet(data);
    }
    private void goodG2B() throws Throwable
    {
        int data;
        if (IO.staticReturnsTrueOrFalse())
        {
            data = 2;
        }
        else
        {
            data = 2;
        }
        HashSet intHashSet = new HashSet(data);
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
