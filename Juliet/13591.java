
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_61a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_61b()).badSource();
        HashMap intHashMap = new HashMap(data);
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        int data = (new CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_61b()).goodG2BSource();
        HashMap intHashMap = new HashMap(data);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
