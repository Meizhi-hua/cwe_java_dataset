
package testcases.CWE690_NULL_Deref_From_Return;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
public class CWE690_NULL_Deref_From_Return__Properties_getProperty_trim_42 extends AbstractTestCase
{
    private String badSource() throws Throwable
    {
        String data;
        FileInputStream streamFileInput = null;
        String propertiesFileName = "./CWE690_NULL_Deref_From_Return__Helper.properties";
        try
        {
            streamFileInput = new FileInputStream(propertiesFileName);
            Properties properties = new Properties();
            properties.load(streamFileInput);
            data = properties.getProperty("CWE690");
        }
        catch (IOException exceptIO)
        {
            IO.writeLine("Could not open properties file: " + propertiesFileName);
            data = ""; 
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
            catch (IOException e)
            {
                IO.logger.log(Level.WARNING, "Error closing FileInputStream", e);
            }
        }
        return data;
    }
    public void bad() throws Throwable
    {
        String data = badSource();
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private String goodG2BSource() throws Throwable
    {
        String data;
        data = "CWE690";
        return data;
    }
    private void goodG2B() throws Throwable
    {
        String data = goodG2BSource();
        String stringTrimmed = data.trim();
        IO.writeLine(stringTrimmed);
    }
    private String goodB2GSource() throws Throwable
    {
        String data;
        FileInputStream streamFileInput = null;
        String propertiesFileName = "./CWE690_NULL_Deref_From_Return__Helper.properties";
        try
        {
            streamFileInput = new FileInputStream(propertiesFileName);
            Properties properties = new Properties();
            properties.load(streamFileInput);
            data = properties.getProperty("CWE690");
        }
        catch (IOException exceptIO)
        {
            IO.writeLine("Could not open properties file: " + propertiesFileName);
            data = ""; 
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
            catch (IOException e)
            {
                IO.logger.log(Level.WARNING, "Error closing FileInputStream", e);
            }
        }
        return data;
    }
    private void goodB2G() throws Throwable
    {
        String data = goodB2GSource();
        if (data != null)
        {
            String stringTrimmed = data.trim();
            IO.writeLine(stringTrimmed);
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
