
package testcases.CWE470_Unsafe_Reflection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE470_Unsafe_Reflection__Environment_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        String data;
        if (privateFive == 5)
        {
            data = System.getenv("ADD");
        }
        else
        {
            data = null;
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (privateFive != 5)
        {
            data = null;
        }
        else
        {
            data = "Testing.test";
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (privateFive == 5)
        {
            data = "Testing.test";
        }
        else
        {
            data = null;
        }
        Class<?> tempClass = Class.forName(data);
        Object tempClassObject = tempClass.newInstance();
        IO.writeLine(tempClassObject.toString()); 
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
