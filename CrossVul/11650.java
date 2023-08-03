
package org.openhab.binding.insteon.internal.device;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.insteon.internal.device.DeviceType.FeatureGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
@NonNullByDefault
@SuppressWarnings("null")
public class DeviceTypeLoader {
    private static final Logger logger = LoggerFactory.getLogger(DeviceTypeLoader.class);
    private HashMap<String, DeviceType> deviceTypes = new HashMap<>();
    private @Nullable static DeviceTypeLoader deviceTypeLoader = null;
    private DeviceTypeLoader() {
    } 
    public @Nullable DeviceType getDeviceType(String aProdKey) {
        return (deviceTypes.get(aProdKey));
    }
    public HashMap<String, DeviceType> getDeviceTypes() {
        return (deviceTypes);
    }
    public void loadDeviceTypesXML(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature("http:
        dbFactory.setFeature("http:
        dbFactory.setFeature("http:
        dbFactory.setXIncludeAware(false);
        dbFactory.setExpandEntityReferences(false);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(in);
        doc.getDocumentElement().normalize();
        Node root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("device")) {
                processDevice((Element) node);
            }
        }
    }
    public void loadDeviceTypesXML(String aFileName) throws ParserConfigurationException, SAXException, IOException {
        File file = new File(aFileName);
        InputStream in = new FileInputStream(file);
        loadDeviceTypesXML(in);
    }
    private void processDevice(Element e) throws SAXException {
        String productKey = e.getAttribute("productKey");
        if (productKey.equals("")) {
            throw new SAXException("device in device_types file has no product key!");
        }
        if (deviceTypes.containsKey(productKey)) {
            logger.warn("overwriting previous definition of device {}", productKey);
            deviceTypes.remove(productKey);
        }
        DeviceType devType = new DeviceType(productKey);
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("model")) {
                devType.setModel(subElement.getTextContent());
            } else if (subElement.getNodeName().equals("description")) {
                devType.setDescription(subElement.getTextContent());
            } else if (subElement.getNodeName().equals("feature")) {
                processFeature(devType, subElement);
            } else if (subElement.getNodeName().equals("feature_group")) {
                processFeatureGroup(devType, subElement);
            }
            deviceTypes.put(productKey, devType);
        }
    }
    private String processFeature(DeviceType devType, Element e) throws SAXException {
        String name = e.getAttribute("name");
        if (name.equals("")) {
            throw new SAXException("feature " + e.getNodeName() + " has feature without name!");
        }
        if (!name.equals(name.toLowerCase())) {
            throw new SAXException("feature name '" + name + "' must be lower case");
        }
        if (!devType.addFeature(name, e.getTextContent())) {
            throw new SAXException("duplicate feature: " + name);
        }
        return (name);
    }
    private String processFeatureGroup(DeviceType devType, Element e) throws SAXException {
        String name = e.getAttribute("name");
        if (name.equals("")) {
            throw new SAXException("feature group " + e.getNodeName() + " has no name attr!");
        }
        String type = e.getAttribute("type");
        if (type.equals("")) {
            throw new SAXException("feature group " + e.getNodeName() + " has no type attr!");
        }
        FeatureGroup fg = new FeatureGroup(name, type);
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("feature")) {
                fg.addFeature(processFeature(devType, subElement));
            } else if (subElement.getNodeName().equals("feature_group")) {
                fg.addFeature(processFeatureGroup(devType, subElement));
            }
        }
        if (!devType.addFeatureGroup(name, fg)) {
            throw new SAXException("duplicate feature group " + name);
        }
        return (name);
    }
    private void logDeviceTypes() {
        for (Entry<String, DeviceType> dt : getDeviceTypes().entrySet()) {
            String msg = String.format("%-10s->", dt.getKey()) + dt.getValue();
            logger.debug("{}", msg);
        }
    }
    @Nullable
    public static synchronized DeviceTypeLoader instance() {
        if (deviceTypeLoader == null) {
            deviceTypeLoader = new DeviceTypeLoader();
            InputStream input = DeviceTypeLoader.class.getResourceAsStream("/device_types.xml");
            try {
                deviceTypeLoader.loadDeviceTypesXML(input);
            } catch (ParserConfigurationException e) {
                logger.warn("parser config error when reading device types xml file: ", e);
            } catch (SAXException e) {
                logger.warn("SAX exception when reading device types xml file: ", e);
            } catch (IOException e) {
                logger.warn("I/O exception when reading device types xml file: ", e);
            }
            logger.debug("loaded {} devices: ", deviceTypeLoader.getDeviceTypes().size());
            deviceTypeLoader.logDeviceTypes();
        }
        return deviceTypeLoader;
    }
}
