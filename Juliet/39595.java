
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_67a extends AbstractTestCase
{
    static class Container
    {
        public int containerOne;
    }
    public void bad() throws Throwable
    {
        int data;
        data = -1;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        int data;
        data = 2;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_67b()).goodG2BSink(dataContainer  );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = -1;
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_67b()).goodB2GSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
