
package testcases.CWE191_Integer_Underflow.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_22b
{
    public void badSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_22a.badPublicStatic)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
    public void goodB2G1Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_22a.goodB2G1PublicStatic)
        {
            data = 0;
        }
        else
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
    }
    public void goodB2G2Sink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_22a.goodB2G2PublicStatic)
        {
            if (data > Integer.MIN_VALUE)
            {
                int result = (int)(data - 1);
                IO.writeLine("result: " + result);
            }
            else
            {
                IO.writeLine("data value is too small to perform subtraction.");
            }
        }
        else
        {
            data = 0;
        }
    }
    public void goodG2BSink(int data , HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        if (CWE191_Integer_Underflow__int_getQueryString_Servlet_sub_22a.goodG2BPublicStatic)
        {
            int result = (int)(data - 1);
            IO.writeLine("result: " + result);
        }
        else
        {
            data = 0;
        }
    }
}
