
package testcases.CWE190_Integer_Overflow.s05;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__short_rand_add_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        short data = 0;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        badPublicStatic = true;
        (new CWE190_Integer_Overflow__short_rand_add_22b()).badSink(data );
    }
    public static boolean goodB2G1PublicStatic = false;
    public static boolean goodB2G2PublicStatic = false;
    public static boolean goodG2BPublicStatic = false;
    public void good() throws Throwable
    {
        goodB2G1();
        goodB2G2();
        goodG2B();
    }
    private void goodB2G1() throws Throwable
    {
        short data = 0;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        goodB2G1PublicStatic = false;
        (new CWE190_Integer_Overflow__short_rand_add_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        short data = 0;
        data = (short)((new java.security.SecureRandom()).nextInt(1+Short.MAX_VALUE-Short.MIN_VALUE)+Short.MIN_VALUE);
        goodB2G2PublicStatic = true;
        (new CWE190_Integer_Overflow__short_rand_add_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        short data = 0;
        data = 2;
        goodG2BPublicStatic = true;
        (new CWE190_Integer_Overflow__short_rand_add_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
