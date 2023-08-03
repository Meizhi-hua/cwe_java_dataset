
package testcases.CWE369_Divide_by_Zero.s02;
import testcasesupport.*;
public class CWE369_Divide_by_Zero__float_zero_divide_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        float data;
        data = 0.0f; 
        float[] dataArray = new float[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__float_zero_divide_66b()).badSink(dataArray  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        float data;
        data = 2.0f;
        float[] dataArray = new float[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__float_zero_divide_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        float data;
        data = 0.0f; 
        float[] dataArray = new float[5];
        dataArray[2] = data;
        (new CWE369_Divide_by_Zero__float_zero_divide_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
