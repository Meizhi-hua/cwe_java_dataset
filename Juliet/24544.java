
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_04 extends AbstractTestCase
{
    private static final boolean PRIVATE_STATIC_FINAL_TRUE = true;
    private static final boolean PRIVATE_STATIC_FINAL_FALSE = false;
    public void bad() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B1() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            data = null;
        }
        else
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodG2B2() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G1() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_FALSE)
        {
            IO.writeLine("Benign, fixed string");
        }
        else
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
            }
        }
    }
    private void goodB2G2() throws Throwable
    {
        String data;
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        }
        else
        {
            data = null;
        }
        if (PRIVATE_STATIC_FINAL_TRUE)
        {
            if (data != null)
            {
                String stringTrimmed = data.trim();
                IO.writeLine(stringTrimmed);
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
