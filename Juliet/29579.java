
package testcases.CWE191_Integer_Underflow.s03;
import testcasesupport.*;
public class CWE191_Integer_Underflow__short_min_sub_81_goodG2B extends CWE191_Integer_Underflow__short_min_sub_81_base
{
    public void action(short data ) throws Throwable
    {
        short result = (short)(data - 1);
        IO.writeLine("result: " + result);
    }
}
