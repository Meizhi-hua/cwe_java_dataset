
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;
import org.apache.commons.lang.StringEscapeUtils;
public class CWE643_Xpath_Injection__database_45 extends AbstractTestCase
{
    private String dataBad;
    private String dataGoodG2B;
    private String dataGoodB2G;
    private void badSink() throws Throwable
    {
        String data = dataBad;
        String xmlFile = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
        {
            xmlFile = "\\src\\testcases\\CWE643_Xpath Injection\\CWE643_Xpath_Injection__Helper.xml";
        }
        else
        {
            xmlFile = "./src/testcases/CWE643_Xpath Injection/CWE643_Xpath_Injection__Helper.xml";
        }
        if (data != null)
        {
            String [] tokens = data.split("||");
            if (tokens.length < 2)
            {
                return;
            }
            String username = tokens[0];
            String password = tokens[1];
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputXml = new InputSource(xmlFile);
            String query = "
                           "' and pass/text()='" + password + "']" +
                           "/secret/text()";
            String secret = (String)xPath.evaluate(query, inputXml, XPathConstants.STRING);
        }
    }
    public void bad() throws Throwable
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
        dataBad = data;
        badSink();
    }
    public void good() throws Throwable
    {
        goodG2B();
        goodB2G();
    }
    private void goodG2BSink() throws Throwable
    {
        String data = dataGoodG2B;
        String xmlFile = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
        {
            xmlFile = "\\src\\testcases\\CWE643_Xpath Injection\\CWE643_Xpath_Injection__Helper.xml";
        }
        else
        {
            xmlFile = "./src/testcases/CWE643_Xpath Injection/CWE643_Xpath_Injection__Helper.xml";
        }
        if (data != null)
        {
            String [] tokens = data.split("||");
            if (tokens.length < 2)
            {
                return;
            }
            String username = tokens[0];
            String password = tokens[1];
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputXml = new InputSource(xmlFile);
            String query = "
                           "' and pass/text()='" + password + "']" +
                           "/secret/text()";
            String secret = (String)xPath.evaluate(query, inputXml, XPathConstants.STRING);
        }
    }
    private void goodG2B() throws Throwable
    {
        String data;
        data = "foo";
        dataGoodG2B = data;
        goodG2BSink();
    }
    private void goodB2GSink() throws Throwable
    {
        String data = dataGoodB2G;
        String xmlFile = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
        {
            xmlFile = "\\src\\testcases\\CWE643_Xpath Injection\\CWE643_Xpath_Injection__Helper.xml";
        }
        else
        {
            xmlFile = "./src/testcases/CWE643_Xpath Injection/CWE643_Xpath_Injection__Helper.xml";
        }
        if (data != null)
        {
            String [] tokens = data.split("||");
            if( tokens.length < 2 )
            {
                return;
            }
            String username = StringEscapeUtils.escapeXml(tokens[0]);
            String password = StringEscapeUtils.escapeXml(tokens[1]);
            XPath xPath = XPathFactory.newInstance().newXPath();
            InputSource inputXml = new InputSource(xmlFile);
            String query = "
                           "' and pass/text()='" + password + "']" +
                           "/secret/text()";
            String secret = (String)xPath.evaluate(query, inputXml, XPathConstants.STRING);
        }
    }
    private void goodB2G() throws Throwable
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
        dataGoodB2G = data;
        goodB2GSink();
    }
    public static void main(String[] args) throws ClassNotFoundException,
           InstantiationException, IllegalAccessException
    {
        mainFromParent(args);
    }
}
