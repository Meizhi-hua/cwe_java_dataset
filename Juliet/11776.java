
package testcases.CWE15_External_Control_of_System_or_Configuration_Setting;
import testcasesupport.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.sql.*;
public class CWE15_External_Control_of_System_or_Configuration_Setting__PropertiesFile_31 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String dataCopy;
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
            dataCopy = data;
        }
        {
            String data = dataCopy;
            Connection dbConnection = null;
            try
            {
                dbConnection = IO.getDBConnection();
                dbConnection.setCatalog(data);
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
            }
            finally
            {
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
        }
    }
    public void good() throws Throwable
    {
        goodG2B();
    }
    private void goodG2B() throws Throwable
    {
        String dataCopy;
        {
            String data;
            data = "foo";
            dataCopy = data;
        }
        {
            String data = dataCopy;
            Connection dbConnection = null;
            try
            {
                dbConnection = IO.getDBConnection();
                dbConnection.setCatalog(data);
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
            }
            finally
            {
                try
                {
                    if (dbConnection != null)
                    {
                        dbConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
            }
        }
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
