
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__connect_tcp_68b
{
    public void badSink() throws Throwable
    {
        String data = CWE470_Unsafe_Reflection__connect_tcp_68a.data;
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void goodG2BSink() throws Throwable
    {
        String data = CWE470_Unsafe_Reflection__connect_tcp_68a.data;
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
}
