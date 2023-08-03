
package testcases.CWE226_Sensitive_Information_Uncleared_Before_Release;
import testcasesupport.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
public class CWE226_Sensitive_Information_Uncleared_Before_Release__basic_15 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        switch (7)
        {
        case 7:
            InputStreamReader readerInputStream = null;
            BufferedReader readerBuffered = null;
            StringBuffer password = new StringBuffer();
            Connection dBConnection = null;
            try
            {
                readerInputStream = new InputStreamReader(System.in, "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                password.append(readerBuffered.readLine());
                dBConnection = DriverManager.getConnection("192.168.105.23", "sa", password.toString());
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
            }
            finally
            {
                try
                {
                    if (dBConnection != null)
                    {
                        dBConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    private void good1() throws Throwable
    {
        switch (8)
        {
        case 7:
            IO.writeLine("Benign, fixed string");
            break;
        default:
            InputStreamReader readerInputStream = null;
            BufferedReader readerBuffered = null;
            StringBuffer password = new StringBuffer();
            Connection dBConnection = null;
            try
            {
                readerInputStream = new InputStreamReader(System.in, "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                password.append(readerBuffered.readLine());
                dBConnection = DriverManager.getConnection("192.168.105.23", "sa", password.toString());
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
            }
            finally
            {
                password.delete(0, password.length());
                try
                {
                    if (dBConnection != null)
                    {
                        dBConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
                }
            }
            break;
        }
    }
    private void good2() throws Throwable
    {
        switch (7)
        {
        case 7:
            InputStreamReader readerInputStream = null;
            BufferedReader readerBuffered = null;
            StringBuffer password = new StringBuffer();
            Connection dBConnection = null;
            try
            {
                readerInputStream = new InputStreamReader(System.in, "UTF-8");
                readerBuffered = new BufferedReader(readerInputStream);
                password.append(readerBuffered.readLine());
                dBConnection = DriverManager.getConnection("192.168.105.23", "sa", password.toString());
            }
            catch (IOException exceptIO)
            {
                IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
            }
            catch (SQLException exceptSql)
            {
                IO.logger.log(Level.WARNING, "Error getting database connection", exceptSql);
            }
            finally
            {
                password.delete(0, password.length());
                try
                {
                    if (dBConnection != null)
                    {
                        dBConnection.close();
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                }
                try
                {
                    if (readerBuffered != null)
                    {
                        readerBuffered.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing BufferedReader", exceptIO);
                }
                try
                {
                    if (readerInputStream != null)
                    {
                        readerInputStream.close();
                    }
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error closing InputStreamReader", exceptIO);
                }
            }
            break;
        default:
            IO.writeLine("Benign, fixed string");
            break;
        }
    }
    public void good() throws Throwable
    {
        good1();
        good2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
