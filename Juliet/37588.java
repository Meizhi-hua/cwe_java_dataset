
package testcases.CWE190_Integer_Overflow.s01;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__int_connect_tcp_square_81_goodG2B extends CWE190_Integer_Overflow__int_connect_tcp_square_81_base
{
    public void action(int data ) throws Throwable
    {
        int result = (int)(data * data);
        IO.writeLine("result: " + result);
    }
}
