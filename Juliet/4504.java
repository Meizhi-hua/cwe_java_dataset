
package testcases.CWE643_Xpath_Injection;
import testcasesupport.*;
import javax.servlet.http.*;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;
import org.apache.commons.lang.StringEscapeUtils;
public class CWE643_Xpath_Injection__console_readLine_81_goodG2B extends CWE643_Xpath_Injection__console_readLine_81_base
{
    public void action(String data ) throws Throwable
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
