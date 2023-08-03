
package testcases.CWE476_NULL_Pointer_Dereference;
import testcasesupport.*;
public class CWE476_NULL_Pointer_Dereference__int_array_22a extends AbstractTestCase
{
    public static boolean badPublicStatic = false;
    public void bad() throws Throwable
    {
        int [] data = null;
        data = null;
        badPublicStatic = true;
        (new CWE476_NULL_Pointer_Dereference__int_array_22b()).badSink(data );
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
        int [] data = null;
        data = null;
        goodB2G1PublicStatic = false;
        (new CWE476_NULL_Pointer_Dereference__int_array_22b()).goodB2G1Sink(data );
    }
    private void goodB2G2() throws Throwable
    {
        int [] data = null;
        data = null;
        goodB2G2PublicStatic = true;
        (new CWE476_NULL_Pointer_Dereference__int_array_22b()).goodB2G2Sink(data );
    }
    private void goodG2B() throws Throwable
    {
        int [] data = null;
        data = new int[5];
        goodG2BPublicStatic = true;
        (new CWE476_NULL_Pointer_Dereference__int_array_22b()).goodG2BSink(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
