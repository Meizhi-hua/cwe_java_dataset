
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
public class CWE690_NULL_Deref_From_Return__Class_String_45 extends AbstractTestCase
{
    private String dataBad;
    private String dataGoodG2B;
    private String dataGoodB2G;
    private void badSink() throws Throwable
    {
        String data = dataBad;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    public void bad() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink() throws Throwable
    {
        String data = dataGoodG2B;
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringGood();
        dataGoodG2B = data;
        goodG2BSink();
    }
    private void goodB2GSink() throws Throwable
    {
        String data = dataGoodB2G;
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = CWE690_NULL_Deref_From_Return__Class_Helper.getStringBad();
        dataGoodB2G = data;
        goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
