
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_HashMap_42 extends AbstractTestCase
{
    private int badSource() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        return data;
    }
    public void bad() throws Throwable
    {
        int data = badSource();
        HashMap intHashMap = new HashMap(data);
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
        HashMap intHashMap = new HashMap(data);
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
