
package testcases.CWE478_Missing_Default_Case_in_Switch;
import testcasesupport.*;
import java.io.*;
import java.security.SecureRandom;
public class CWE478_Missing_Default_Case_in_Switch__basic_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        if (privateFive == 5)
        {
            String stringIntValue = "";
            int x = (new SecureRandom()).nextInt(3);
            switch (x)
            {
            case 0:
                stringIntValue = "0";
                break;
            case 1:
                stringIntValue = "1";
                break;
            }
            IO.writeLine(stringIntValue);
        }
    }
    private void good1() throws Throwable
    {
        if (privateFive != 5)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            String stringIntValue = "";
            int x = (new SecureRandom()).nextInt(3);
            switch (x)
            {
            case 0:
                stringIntValue = "0";
                break;
            case 1:
                stringIntValue = "1";
                break;
            default:
                stringIntValue = "2";
            }
            IO.writeLine(stringIntValue);
        }
    }
    private void good2() throws Throwable
    {
        if (privateFive == 5)
        {
            String stringIntValue = "";
            int x = (new SecureRandom()).nextInt(3);
            switch (x)
            {
            case 0:
                stringIntValue = "0";
                break;
            case 1:
                stringIntValue = "1";
                break;
            default:
                stringIntValue = "2";
            }
            IO.writeLine(stringIntValue);
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
