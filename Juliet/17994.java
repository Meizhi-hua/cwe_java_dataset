
package testcases.CWE563_Unused_Variable;
import testcasesupport.*;
public class CWE563_Unused_Variable__unused_parameter_value_01 extends AbstractTestCase 
{
    private void helperBad(int intBad) 
    {
        intBad = 0;
        IO.writeLine("" + intBad);
    }
    public void bad()
    {
        helperBad(5);
    }
    private void helperGood1(int intGood1) 
    {
        IO.writeLine("" + intGood1);
    }
    private void good1() 
    {
        helperGood1(10);
    }
    public void good()  
    {
        good1();
    }    
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException 
    {
        mainFromParent(args);
    }
}
