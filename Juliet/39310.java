
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;
import org.apache.commons.lang.StringEscapeUtils;
public class CWE643_Xpath_Injection__connect_tcp_12 extends AbstractTestCase
{
    public void bad() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = ""; 
            {
                Socket socket = null;
                BufferedReader readerBuffered = null;
                InputStreamReader readerInputStream = null;
                try
                {
                    socket = new Socket("host.example.org", 39544);
                    readerInputStream = new InputStreamReader(socket.getInputStream(), "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    data = readerBuffered.readLine();
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
                }
                finally
                {
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
                    try
                    {
                        if (socket != null)
                        {
                            socket.close();
                        }
                    }
                    catch (IOException exceptIO)
                    {
                        IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
                    }
                }
            }
        }
        else
        {
            data = "foo";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
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
        else
        {
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
    }
    private void goodG2B() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = "foo";
        }
        else
        {
            data = "foo";
        }
        if(IO.staticReturnsTrueOrFalse())
        {
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
        else
        {
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
    }
    private void goodB2G() throws Throwable
    {
        String data;
        if(IO.staticReturnsTrueOrFalse())
        {
            data = ""; 
            {
                Socket socket = null;
                BufferedReader readerBuffered = null;
                InputStreamReader readerInputStream = null;
                try
                {
                    socket = new Socket("host.example.org", 39544);
                    readerInputStream = new InputStreamReader(socket.getInputStream(), "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    data = readerBuffered.readLine();
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
                }
                finally
                {
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
                    try
                    {
                        if (socket != null)
                        {
                            socket.close();
                        }
                    }
                    catch (IOException exceptIO)
                    {
                        IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
                    }
                }
            }
        }
        else
        {
            data = ""; 
            {
                Socket socket = null;
                BufferedReader readerBuffered = null;
                InputStreamReader readerInputStream = null;
                try
                {
                    socket = new Socket("host.example.org", 39544);
                    readerInputStream = new InputStreamReader(socket.getInputStream(), "UTF-8");
                    readerBuffered = new BufferedReader(readerInputStream);
                    data = readerBuffered.readLine();
                }
                catch (IOException exceptIO)
                {
                    IO.logger.log(Level.WARNING, "Error with stream reading", exceptIO);
                }
                finally
                {
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
                    try
                    {
                        if (socket != null)
                        {
                            socket.close();
                        }
                    }
                    catch (IOException exceptIO)
                    {
                        IO.logger.log(Level.WARNING, "Error closing Socket", exceptIO);
                    }
                }
            }
        }
        if(IO.staticReturnsTrueOrFalse())
        {
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
        else
        {
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
