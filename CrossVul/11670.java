
package org.openhab.binding.fsinternetradio.internal.radio;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class FrontierSiliconRadioApiResult {
    final Document xmlDoc;
    private final Logger logger = LoggerFactory.getLogger(FrontierSiliconRadioApiResult.class);
    public FrontierSiliconRadioApiResult(String requestResultString) throws IOException {
        Document xml = null;
        try {
            xml = getXmlDocFromString(requestResultString);
        } catch (Exception e) {
            logger.trace("converting to XML failed: '{}' with {}: {}", requestResultString, e.getClass().getName(),
                    e.getMessage());
            logger.debug("converting to XML failed with {}: {}", e.getClass().getName(), e.getMessage());
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new IOException(e);
        }
        xmlDoc = xml;
    }
    private String getStatus() {
        final Element fsApiResult = (Element) xmlDoc.getElementsByTagName("fsapiResponse").item(0);
        final Element statusNode = (Element) fsApiResult.getElementsByTagName("status").item(0);
        final String status = getCharacterDataFromElement(statusNode);
        logger.trace("status is: {}", status);
        return status;
    }
    public boolean isStatusOk() {
        return ("FS_OK").equals(getStatus());
    }
    public boolean getValueU8AsBoolean() {
        try {
            final Element fsApiResult = (Element) xmlDoc.getElementsByTagName("fsapiResponse").item(0);
            final Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
            final Element u8Node = (Element) valueNode.getElementsByTagName("u8").item(0);
            final String value = getCharacterDataFromElement(u8Node);
            logger.trace("value is: {}", value);
            return "1".equals(value);
        } catch (Exception e) {
            logger.error("getting Value.U8 failed with {}: {}", e.getClass().getName(), e.getMessage());
            return false;
        }
    }
    public int getValueU8AsInt() {
        try {
            final Element fsApiResult = (Element) xmlDoc.getElementsByTagName("fsapiResponse").item(0);
            final Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
            final Element u8Node = (Element) valueNode.getElementsByTagName("u8").item(0);
            final String value = getCharacterDataFromElement(u8Node);
            logger.trace("value is: {}", value);
            return Integer.parseInt(value);
        } catch (Exception e) {
            logger.error("getting Value.U8 failed with {}: {}", e.getClass().getName(), e.getMessage());
            return 0;
        }
    }
    public int getValueU32AsInt() {
        try {
            final Element fsApiResult = (Element) xmlDoc.getElementsByTagName("fsapiResponse").item(0);
            final Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
            final Element u32Node = (Element) valueNode.getElementsByTagName("u32").item(0);
            final String value = getCharacterDataFromElement(u32Node);
            logger.trace("value is: {}", value);
            return Integer.parseInt(value);
        } catch (Exception e) {
            logger.error("getting Value.U32 failed with {}: {}", e.getClass().getName(), e.getMessage());
            return 0;
        }
    }
    public String getValueC8ArrayAsString() {
        try {
            final Element fsApiResult = (Element) xmlDoc.getElementsByTagName("fsapiResponse").item(0);
            final Element valueNode = (Element) fsApiResult.getElementsByTagName("value").item(0);
            final Element c8Array = (Element) valueNode.getElementsByTagName("c8_array").item(0);
            final String value = getCharacterDataFromElement(c8Array);
            logger.trace("value is: {}", value);
            return value;
        } catch (Exception e) {
            logger.error("getting Value.c8array failed with {}: {}", e.getClass().getName(), e.getMessage());
            return "";
        }
    }
    public String getSessionId() {
        final NodeList sessionIdTagList = xmlDoc.getElementsByTagName("sessionId");
        final String givenSessId = getCharacterDataFromElement((Element) sessionIdTagList.item(0));
        return givenSessId;
    }
    private Document getXmlDocFromString(String xmlString)
            throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http:
        factory.setFeature("http:
        factory.setFeature("http:
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document xmlDocument = builder.parse(new InputSource(new StringReader(xmlString)));
        return xmlDocument;
    }
    private static String getCharacterDataFromElement(Element e) {
        final Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            final CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }
}
