
package testcases.CWE190_Integer_Overflow.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE190_Integer_Overflow__long_rand_square_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        long data = 0L;
        data = (new java.security.SecureRandom()).nextLong();
        badPublicStatic = true;
        (new CWE190_Integer_Overflow__long_rand_square_22b()).badSink(data );
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
        long data = 0L;
        data = (new java.security.SecureRandom()).nextLong();
        goodB2G1PublicStatic = false;
        (new CWE190_Integer_Overflow__long_rand_square_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        long data = 0L;
        data = (new java.security.SecureRandom()).nextLong();
        goodB2G2PublicStatic = true;
        (new CWE190_Integer_Overflow__long_rand_square_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        long data = 0L;
        data = 2;
        goodG2BPublicStatic = true;
        (new CWE190_Integer_Overflow__long_rand_square_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
