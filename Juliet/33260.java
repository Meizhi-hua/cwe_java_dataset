
package testcases.CWE606_Unchecked_Loop_Condition;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE606_Unchecked_Loop_Condition__Property_68b
{
    public void badSink() throws Throwable
    {
        String data = CWE606_Unchecked_Loop_Condition__Property_68a.data;
        int numberOfLoops;
        try
        {
            numberOfLoops = Integer.parseInt(data);
        }
        catch (NumberFormatException exceptNumberFormat)
        {
            IO.writeLine("Invalid response. Numeric input expected. Assuming 1.");
            numberOfLoops = 1;
        }
        for (int i=0; i < numberOfLoops; i++)
        {
            IO.writeLine("hello world");
        }
    }
    public void goodG2BSink() throws Throwable
    {
        String data = CWE606_Unchecked_Loop_Condition__Property_68a.data;
        int numberOfLoops;
        try
        {
            numberOfLoops = Integer.parseInt(data);
        }
        catch (NumberFormatException exceptNumberFormat)
        {
            IO.writeLine("Invalid response. Numeric input expected. Assuming 1.");
            numberOfLoops = 1;
        }
        for (int i=0; i < numberOfLoops; i++)
        {
            IO.writeLine("hello world");
        }
    }
    public void goodB2GSink() throws Throwable
    {
        String data = CWE606_Unchecked_Loop_Condition__Property_68a.data;
        int numberOfLoops;
        try
        {
            numberOfLoops = Integer.parseInt(data);
        }
        catch (NumberFormatException exceptNumberFormat)
        {
            IO.writeLine("Invalid response. Numeric input expected. Assuming 1.");
            numberOfLoops = 1;
        }
        if (numberOfLoops >= 0 && numberOfLoops <= 5)
        {
            for (int i=0; i < numberOfLoops; i++)
            {
                IO.writeLine("hello world");
            }
        }
    }
}
