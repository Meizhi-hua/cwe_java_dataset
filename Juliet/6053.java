
package testcases.CWE89_SQL_Injection.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__Environment_executeUpdate_71a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE89_SQL_Injection__Environment_executeUpdate_71b()).badSink((Object)data  );
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        (new CWE89_SQL_Injection__Environment_executeUpdate_71b()).goodG2BSink((Object)data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        (new CWE89_SQL_Injection__Environment_executeUpdate_71b()).goodB2GSink((Object)data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
