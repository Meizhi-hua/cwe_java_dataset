
package testcases.CWE36_Absolute_Path_Traversal;
import testcasesupport.*;
import java.io.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
public class CWE36_Absolute_Path_Traversal__PropertiesFile_22b
{
    public String badSource() throws Throwable
    {
        String data;
        if (CWE36_Absolute_Path_Traversal__PropertiesFile_22a.badPublicStatic)
        {
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
        }
        else
        {
            data = null;
        }
        return data;
    }
    public String goodG2B1Source() throws Throwable
    {
        String data;
        if (CWE36_Absolute_Path_Traversal__PropertiesFile_22a.goodG2B1PublicStatic)
        {
            data = null;
        }
        else
        {
            data = "foo";
        }
        return data;
    }
    public String goodG2B2Source() throws Throwable
    {
        String data;
        if (CWE36_Absolute_Path_Traversal__PropertiesFile_22a.goodG2B2PublicStatic)
        {
            data = "foo";
        }
        else
        {
            data = null;
        }
        return data;
    }
}
