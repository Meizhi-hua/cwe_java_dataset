
package testcases.CWE789_Uncontrolled_Mem_Alloc.s01;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
import java.util.ArrayList;
public class CWE789_Uncontrolled_Mem_Alloc__getCookies_Servlet_ArrayList_74b
{
    public void badSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        ArrayList intArrayList = new ArrayList(data);
    }
    public void goodG2BSink(HashMap<Integer,Integer> dataHashMap , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        int data = dataHashMap.get(2);
        ArrayList intArrayList = new ArrayList(data);
    }
}
