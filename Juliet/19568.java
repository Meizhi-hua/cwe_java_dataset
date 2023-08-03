
package testcases.CWE129_Improper_Validation_of_Array_Index.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE129_Improper_Validation_of_Array_Index__connect_tcp_array_read_no_check_51b
{
    public void badSink(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
    public void goodG2BSink(int data ) throws Throwable
    {
        int array[] = { 0, 1, 2, 3, 4 };
        IO.writeLine(array[data]);
    }
    public void goodB2GSink(int data ) throws Throwable
    {
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
