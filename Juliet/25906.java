
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__connect_tcp_ArrayList_72b
{
    public void badSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(Vector<Integer> dataVector ) throws Throwable
    {
        int data = dataVector.remove(2);
        ArrayList intArrayList = new ArrayList(data);
    }
}
