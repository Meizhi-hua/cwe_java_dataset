
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__byte_console_readLine_square_81_goodG2B extends CWE190_Integer_Overflow__byte_console_readLine_square_81_base
{
    public void action(byte data ) throws Throwable
    {
        byte result = (byte)(data * data);
        IO.writeLine("result: " + result);
    }
}
