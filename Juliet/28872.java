
package testcases.CWE570_Expression_Always_False;
import testcasesupport.AbstractTestCase;
import testcasesupport.IO;
import java.security.SecureRandom;
public class CWE570_Expression_Always_False__static_final_five_01 extends AbstractTestCase 
{
    public void bad()
    {
        if (IO.STATIC_FINAL_FIVE != 5)
        {
            IO.writeLine("never prints");
        }
    }
    public void good()
    {
        good1();
    }
    private void good1()
    {
        if ((new SecureRandom()).nextInt() != IO.STATIC_FINAL_FIVE)
        {
            IO.writeLine("sometimes prints");
        }
    }
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
