
package testcases.CWE129_Improper_Validation_of_Array_Index.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__database_array_read_no_check_67b
{
    public void badSink(CWE129_Improper_Validation_of_Array_Index__database_array_read_no_check_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
    public void goodG2BSink(CWE129_Improper_Validation_of_Array_Index__database_array_read_no_check_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
    public void goodB2GSink(CWE129_Improper_Validation_of_Array_Index__database_array_read_no_check_67a.Container dataContainer ) throws Throwable
    {
        int data = dataContainer.containerOne;
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
