
package testcases.CWE89_SQL_Injection.s04;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__Property_execute_51a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        (new CWE89_SQL_Injection__Property_execute_51b()).badSink(data  );
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
        (new CWE89_SQL_Injection__Property_execute_51b()).goodG2BSink(data  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        (new CWE89_SQL_Injection__Property_execute_51b()).goodB2GSink(data  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
