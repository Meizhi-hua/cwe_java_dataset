
package testcases.CWE15_External_Control_of_System_or_Configuration_Setting;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
public class CWE15_External_Control_of_System_or_Configuration_Setting__Property_67b
{
    public void badSink(CWE15_External_Control_of_System_or_Configuration_Setting__Property_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
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
    public void goodG2BSink(CWE15_External_Control_of_System_or_Configuration_Setting__Property_67a.Container dataContainer ) throws Throwable
    {
        String data = dataContainer.containerOne;
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
