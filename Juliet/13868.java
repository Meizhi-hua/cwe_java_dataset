
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__getCookies_Servlet_72b
{
    public void badSink(Vector<String> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataVector.remove(2);
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void goodG2BSink(Vector<String> dataVector , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data = dataVector.remove(2);
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
}
