
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_modulo_10 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        if (IO.staticTrue)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (IO.staticTrue)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (IO.staticFalse)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        if (IO.staticTrue)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (IO.staticTrue)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        if (IO.staticTrue)
        {
            IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        if (IO.staticTrue)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (IO.staticFalse)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != 0)
            {
                IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        if (IO.staticTrue)
        {
            data = (new SecureRandom()).nextInt();
        }
        else
        {
            data = 0;
        }
        if (IO.staticTrue)
        {
            if (data != 0)
            {
                IO.writeLine("100%" + data + " = " + (100 % data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a modulo by zero");
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
        goodB2G1();
        goodB2G2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
