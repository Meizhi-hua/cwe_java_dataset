
package testcases.CWE90_LDAP_Injection;
import testcasesupport.*;
import java.util.Vector;
import javax.servlet.http.*;
public class CWE90_LDAP_Injection__Property_72a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getProperty("user.home");
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE90_LDAP_Injection__Property_72b()).badSink(dataVector  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        Vector<String> dataVector = new Vector<String>(5);
        dataVector.add(0, data);
        dataVector.add(1, data);
        dataVector.add(2, data);
        (new CWE90_LDAP_Injection__Property_72b()).goodG2BSink(dataVector  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
