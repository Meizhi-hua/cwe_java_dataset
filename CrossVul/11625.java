
package org.openhab.binding.gce.internal.model;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.gce.internal.handler.Ipx800EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
@NonNullByDefault
public class StatusFileInterpreter {
    private static final String URL_TEMPLATE = "http:
    private final Logger logger = LoggerFactory.getLogger(StatusFileInterpreter.class);
    private final String hostname;
    private @Nullable Document doc;
    private final Ipx800EventListener listener;
    public static enum StatusEntry {
        VERSION,
        CONFIG_MAC;
    }
    public StatusFileInterpreter(String hostname, Ipx800EventListener listener) {
        this.hostname = hostname;
        this.listener = listener;
    }
    public void read() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http:
            factory.setFeature("http:
            factory.setFeature("http:
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            String statusPage = HttpUtil.executeUrl("GET", String.format(URL_TEMPLATE, hostname), 5000);
            InputStream inputStream = new ByteArrayInputStream(statusPage.getBytes());
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
            doc = document;
            pushDatas();
            inputStream.close();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.warn("Unable to read IPX800 status page : {}", e.getMessage());
            doc = null;
        }
    }
    private void pushDatas() {
        Element root = getRoot();
        if (root != null) {
            PortDefinition.asStream().forEach(portDefinition -> {
                List<Node> xmlNodes = getMatchingNodes(root.getChildNodes(), portDefinition.getNodeName());
                xmlNodes.forEach(xmlNode -> {
                    String sPortNum = xmlNode.getNodeName().replace(portDefinition.getNodeName(), "");
                    int portNum = Integer.parseInt(sPortNum) + 1;
                    double value = Double.parseDouble(xmlNode.getTextContent().replace("dn", "1").replace("up", "0"));
                    listener.dataReceived(String.format("%s%d", portDefinition.getPortName(), portNum), value);
                });
            });
        }
    }
    public String getElement(StatusEntry entry) {
        Element root = getRoot();
        if (root != null) {
            return root.getElementsByTagName(entry.name().toLowerCase()).item(0).getTextContent();
        } else {
            return "";
        }
    }
    private List<Node> getMatchingNodes(NodeList nodeList, String criteria) {
        return IntStream.range(0, nodeList.getLength()).boxed().map(nodeList::item)
                .filter(node -> node.getNodeName().startsWith(criteria))
                .sorted(Comparator.comparing(o -> o.getNodeName())).collect(Collectors.toList());
    }
    public int getMaxNumberofNodeType(PortDefinition portDefinition) {
        Element root = getRoot();
        if (root != null) {
            List<Node> filteredNodes = getMatchingNodes(root.getChildNodes(), portDefinition.getNodeName());
            return filteredNodes.size();
        }
        return 0;
    }
    private @Nullable Element getRoot() {
        if (doc == null) {
            read();
        }
        if (doc != null) {
            return doc.getDocumentElement();
        }
        return null;
    }
}
