
package testcases.CWE506_Embedded_Malicious_Code;
import testcasesupport.*;
import org.apache.commons.codec.binary.Base64;
import java.io.IOException;
import java.util.logging.Level;
public class CWE506_Embedded_Malicious_Code__base64_encoded_payload_01 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String encodedPayload = "Y2FsYy5leGU=";
        try
        {
            Runtime.getRuntime().exec(new String(Base64.decodeBase64(encodedPayload), "UTF-8"));
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error executing command", exceptIO);
        }
    }
    public void good() throws Throwable
    {
        good1();
    }
    private void good1() throws Throwable
    {
        String decodedPayload = "calc.exe";
        try
        {
            Runtime.getRuntime().exec(decodedPayload);
        }
        catch (IOException exceptIO)
        {
            IO.logger.log(Level.WARNING, "Error executing command", exceptIO);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
