
package testcases.CWE197_Numeric_Truncation_Error.s01;
import testcasesupport.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
public class CWE197_Numeric_Truncation_Error__int_database_to_short_07 extends AbstractTestCase
{
    private int privateFive = 5;
    public void bad() throws Throwable
    {
        int data;
        if (privateFive == 5)
        {
            data = Integer.MIN_VALUE; 
            {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try
                {
                    connection = IO.getDBConnection();
                    preparedStatement = connection.prepareStatement("select name from users where id=0");
                    resultSet = preparedStatement.executeQuery();
                    String stringNumber = resultSet.getString(1);
                    if (stringNumber != null) 
                    {
                        try
                        {
                            data = Integer.parseInt(stringNumber.trim());
                        }
                        catch (NumberFormatException exceptNumberFormat)
                        {
                            IO.logger.log(Level.WARNING, "Number format exception parsing data from string", exceptNumberFormat);
                        }
                    }
                }
                catch (SQLException exceptSql)
                {
                    IO.logger.log(Level.WARNING, "Error with SQL statement", exceptSql);
                }
                finally
                {
                    try
                    {
                        if (resultSet != null)
                        {
                            resultSet.close();
                        }
                    }
                    catch (SQLException exceptSql)
                    {
                        IO.logger.log(Level.WARNING, "Error closing ResultSet", exceptSql);
                    }
                    try
                    {
                        if (preparedStatement != null)
                        {
                            preparedStatement.close();
                        }
                    }
                    catch (SQLException exceptSql)
                    {
                        IO.logger.log(Level.WARNING, "Error closing PreparedStatement", exceptSql);
                    }
                    try
                    {
                        if (connection != null)
                        {
                            connection.close();
                        }
                    }
                    catch (SQLException exceptSql)
                    {
                        IO.logger.log(Level.WARNING, "Error closing Connection", exceptSql);
                    }
                }
            }
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B1() throws Throwable
    {
        int data;
        if (privateFive != 5)
        {
            data = 0;
        }
        else
        {
            data = 2;
        }
        {
            IO.writeLine((short)data);
        }
    }
    private void goodG2B2() throws Throwable
    {
        int data;
        if (privateFive == 5)
        {
            data = 2;
        }
        else
        {
            data = 0;
        }
        {
            IO.writeLine((short)data);
        }
    }
    public void good() throws Throwable
    {
        goodG2B1();
        goodG2B2();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
