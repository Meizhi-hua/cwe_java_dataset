
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
import java.util.HashMap;
public class CWE789_Uncontrolled_Mem_Alloc__connect_tcp_HashMap_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        HashMap intHashMap = new HashMap(data);
    }
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        HashMap intHashMap = new HashMap(data);
    }
}
