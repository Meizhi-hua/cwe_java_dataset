
package testcases.CWE90_LDAP_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE90_LDAP_Injection__Environment_67a extends AbstractTestCase
{
    static class Container
    {
        public String containerOne;
    }
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE90_LDAP_Injection__Environment_67b()).badSink(dataContainer  );
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        Container dataContainer = new Container();
        dataContainer.containerOne = data;
        (new CWE90_LDAP_Injection__Environment_67b()).goodG2BSink(dataContainer  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
