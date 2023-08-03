
package testcases.CWE369_Divide_by_Zero.s03;
import testcasesupport.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
public class CWE369_Divide_by_Zero__int_random_divide_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = (new SecureRandom()).nextInt();
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        switch (5)
        {
        case 6:
            data = 0;
            break;
        default:
            data = 2;
            break;
        }
        switch (7)
        {
        case 7:
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = 2;
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            IO.writeLine("bad: 100/" + data + " = " + (100 / data) + "\n");
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void goodB2G1() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = (new SecureRandom()).nextInt();
            break;
        default:
            data = 0;
            break;
        }
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            if (data != 0)
            {
                IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
            }
            break;
        }
    }
    private void goodB2G2() throws Throwable
    {
        int data;
        switch (6)
        {
        case 6:
            data = (new SecureRandom()).nextInt();
            break;
        default:
            data = 0;
            break;
        }
        switch (7)
        {
        case 7:
            if (data != 0)
            {
                IO.writeLine("100/" + data + " = " + (100 / data) + "\n");
            }
            else
            {
                IO.writeLine("This would result in a divide by zero");
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
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
