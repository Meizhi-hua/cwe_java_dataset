
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_init_variable_long_81a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        long data;
        data = 5L;
        CWE563_Unused_Variable__unused_init_variable_long_81_base baseObject = new CWE563_Unused_Variable__unused_init_variable_long_81_bad();
        baseObject.action(data );
    }
    public void good() throws Throwable
    {
        goodB2G();
    }
    private void goodB2G() throws Throwable
    {
        long data;
        data = 5L;
        CWE563_Unused_Variable__unused_init_variable_long_81_base baseObject = new CWE563_Unused_Variable__unused_init_variable_long_81_goodB2G();
        baseObject.action(data );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
