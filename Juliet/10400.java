
package testcases.CWE113_HTTP_Response_Splitting.s01;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.net.URLEncoder;
public class CWE113_HTTP_Response_Splitting__database_addCookieServlet_17 extends AbstractTestCaseServlet
{
    public void bad(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try
            {
                connection = IO.getDBConnection();
                preparedStatement = connection.prepareStatement("select name from users where id=0");
                resultSet = preparedStatement.executeQuery();
                data = resultSet.getString(1);
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
        for (int j = 0; j < 1; j++)
        {
            if (data != null)
            {
                Cookie cookieSink = new Cookie("lang", data);
                response.addCookie(cookieSink);
            }
        }
    }
    private void goodG2B(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = "foo";
        for (int j = 0; j < 1; j++)
        {
            if (data != null)
            {
                Cookie cookieSink = new Cookie("lang", data);
                response.addCookie(cookieSink);
            }
        }
    }
    private void goodB2G(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        String data;
        data = ""; 
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try
            {
                connection = IO.getDBConnection();
                preparedStatement = connection.prepareStatement("select name from users where id=0");
                resultSet = preparedStatement.executeQuery();
                data = resultSet.getString(1);
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
        for (int k = 0; k < 1; k++)
        {
            if (data != null)
            {
                Cookie cookieSink = new Cookie("lang", URLEncoder.encode(data, "UTF-8"));
                response.addCookie(cookieSink);
            }
        }
    }
    public void good(HttpServletRequest request, HttpServletResponse response) throws Throwable
    {
        goodG2B(request, response);
        goodB2G(request, response);
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
