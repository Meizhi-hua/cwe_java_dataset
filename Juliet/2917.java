
package testcases.CWE134_Uncontrolled_Format_String.s01;
import testcasesupport.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
public class CWE134_Uncontrolled_Format_String__PropertiesFile_format_66a extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        data = ""; 
        {
            Properties properties = new Properties();
            FileInputStream streamFileInput = null;
            try
            {
                streamFileInput = new FileInputStream("../common/config.properties");
                properties.load(streamFileInput);
                data = properties.getProperty("data");
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            finally
            {
                try
                {
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
                }
            }
        }
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE134_Uncontrolled_Format_String__PropertiesFile_format_66b()).badSink(dataArray  );
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
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE134_Uncontrolled_Format_String__PropertiesFile_format_66b()).goodG2BSink(dataArray  );
    }
    private void goodB2G() throws Throwable
    {
        String data;
        data = ""; 
        {
            Properties properties = new Properties();
            FileInputStream streamFileInput = null;
            try
            {
                streamFileInput = new FileInputStream("../common/config.properties");
                properties.load(streamFileInput);
                data = properties.getProperty("data");
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            finally
            {
                try
                {
                    if (streamFileInput != null)
                    {
                        streamFileInput.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing FileInputStream", exceptIO);
                }
            }
        }
        String[] dataArray = new String[5];
        dataArray[2] = data;
        (new CWE134_Uncontrolled_Format_String__PropertiesFile_format_66b()).goodB2GSink(dataArray  );
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
