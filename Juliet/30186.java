
package testcases.CWE129_Improper_Validation_of_Array_Index.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68b
{
    public void badSink() throws Throwable
    {
        int data = CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68a.data;
        int array[] = { 0, 1, 2, 3, 4 };
        if (data < array.length)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
    public void goodG2BSink() throws Throwable
    {
        int data = CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68a.data;
        int array[] = { 0, 1, 2, 3, 4 };
        if (data < array.length)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
    public void goodB2GSink() throws Throwable
    {
        int data = CWE129_Improper_Validation_of_Array_Index__negative_fixed_array_read_check_max_68a.data;
        int array[] = { 0, 1, 2, 3, 4 };
        if (data >= 0 && data < array.length)
        {
            IO.writeLine(array[data]);
        }
        else
        {
            IO.writeLine("Array index out of bounds");
        }
    }
}
