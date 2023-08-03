
package testcases.CWE89_SQL_Injection.s02;
import testcasesupport.*;
import javax.servlet.http.*;
public class CWE89_SQL_Injection__Environment_executeUpdate_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        CWE89_SQL_Injection__Environment_executeUpdate_81_base baseObject = new CWE89_SQL_Injection__Environment_executeUpdate_81_bad();
        baseObject.action(data );
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
        CWE89_SQL_Injection__Environment_executeUpdate_81_base baseObject = new CWE89_SQL_Injection__Environment_executeUpdate_81_goodG2B();
        baseObject.action(data );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = System.getenv("ADD");
        CWE89_SQL_Injection__Environment_executeUpdate_81_base baseObject = new CWE89_SQL_Injection__Environment_executeUpdate_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
