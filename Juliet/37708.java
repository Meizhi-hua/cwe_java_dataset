
package testcases.CWE400_Resource_Exhaustion.s02;
import testcasesupport.*;
import java.util.HashMap;
import javax.servlet.http.*;
public class CWE400_Resource_Exhaustion__PropertiesFile_for_loop_74b
{
    public void badSink(HashMap<Integer,Integer> countHashMap ) throws Throwable
    {
        int count = countHashMap.get(2);
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodG2BSink(HashMap<Integer,Integer> countHashMap ) throws Throwable
    {
        int count = countHashMap.get(2);
        int i = 0;
        for (i = 0; i < count; i++)
        {
            IO.writeLine("Hello");
        }
    }
    public void goodB2GSink(HashMap<Integer,Integer> countHashMap ) throws Throwable
    {
        int count = countHashMap.get(2);
        int i = 0;
        if (count > 0 && count <= 20)
        {
            for (i = 0; i < count; i++)
            {
                IO.writeLine("Hello");
            }
        }
    }
}
