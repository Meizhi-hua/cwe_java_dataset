
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__large_fixed_array_read_check_min_54a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        int data;
        data = 100;
        (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_read_check_min_54b()).badSink(data );
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
        (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_read_check_min_54b()).goodG2BSink(data );
    }
    private void goodB2G() throws Throwable
    {
        int data;
        data = 100;
        (new CWE129_Improper_Validation_of_Array_Index__large_fixed_array_read_check_min_54b()).goodB2GSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
