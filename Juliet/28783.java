
package testcases.CWE789_Uncontrolled_Mem_Alloc.s02;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__max_value_ArrayList_45 extends AbstractTestCase
{
    private int dataBad;
    private int dataGoodG2B;
    private void badSink() throws Throwable
    {
        int data = dataBad;
        ArrayList intArrayList = new ArrayList(data);
    }
    public void bad() throws Throwable
    {
        int data;
        data = Integer.MAX_VALUE;
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2BSink() throws Throwable
    {
        int data = dataGoodG2B;
        ArrayList intArrayList = new ArrayList(data);
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        dataGoodG2B = data;
        goodG2BSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
