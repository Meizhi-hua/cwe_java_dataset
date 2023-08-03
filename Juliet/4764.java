
package testcases.CWE129_Improper_Validation_of_Array_Index.s03;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__large_fixed_array_write_no_check_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE129_Improper_Validation_of_Array_Index__large_fixed_array_write_no_check_68a.data;
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE129_Improper_Validation_of_Array_Index__large_fixed_array_write_no_check_68a.data;
        int array[] = { 0, 1, 2, 3, 4 };
        array[data] = 42;
    }
    public void goodB2GSink() throws Throwable
    {
        int data = CWE129_Improper_Validation_of_Array_Index__large_fixed_array_write_no_check_68a.data;
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0 && data < array.length)
        {
            array[data] = 42;
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
