
package org.openhab.binding.insteon.internal.message;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.insteon.internal.utils.Pair;
import org.openhab.binding.insteon.internal.utils.Utils.DataTypeParser;
import org.openhab.binding.insteon.internal.utils.Utils.ParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
@NonNullByDefault
@SuppressWarnings("null")
public class XMLMessageReader {
    public static HashMap<String, Msg> readMessageDefinitions(InputStream input)
            throws IOException, ParsingException, FieldException {
        HashMap<String, Msg> messageMap = new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setFeature("http:
            dbFactory.setFeature("http:
            dbFactory.setFeature("http:
            dbFactory.setXIncludeAware(false);
            dbFactory.setExpandEntityReferences(false);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();
            Node root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals("msg")) {
                        Pair<String, Msg> msgDef = readMessageDefinition((Element) node);
                        messageMap.put(msgDef.getKey(), msgDef.getValue());
                    }
                }
            }
        } catch (SAXException e) {
            throw new ParsingException("Failed to parse XML!", e);
        } catch (ParserConfigurationException e) {
            throw new ParsingException("Got parser config exception! ", e);
        }
        return messageMap;
    }
    private static Pair<String, Msg> readMessageDefinition(Element msg) throws FieldException, ParsingException {
        int length = 0;
        int hlength = 0;
        LinkedHashMap<Field, Object> fieldMap = new LinkedHashMap<>();
        String dir = msg.getAttribute("direction");
        String name = msg.getAttribute("name");
        Msg.Direction direction = Msg.Direction.getDirectionFromString(dir);
        if (msg.hasAttribute("length")) {
            length = Integer.parseInt(msg.getAttribute("length"));
        }
        NodeList nodes = msg.getChildNodes();
        int offset = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("header")) {
                    int o = readHeaderElement((Element) node, fieldMap);
                    hlength = o;
                    offset += o;
                } else {
                    Pair<Field, Object> field = readField((Element) node, offset);
                    fieldMap.put(field.getKey(), field.getValue());
                    offset += field.getKey().getType().getSize();
                }
            }
        }
        if (offset != length) {
            throw new ParsingException(
                    "Actual msg length " + offset + " differs from given msg length " + length + "!");
        }
        if (length == 0) {
            length = offset;
        }
        return new Pair<>(name, createMsg(fieldMap, length, hlength, direction));
    }
    private static int readHeaderElement(Element header, LinkedHashMap<Field, Object> fields) throws ParsingException {
        int offset = 0;
        int headerLen = Integer.parseInt(header.getAttribute("length"));
        NodeList nodes = header.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                @Nullable
                Pair<Field, Object> definition = readField((Element) node, offset);
                if (definition != null) {
                    offset += definition.getKey().getType().getSize();
                    fields.put(definition.getKey(), definition.getValue());
                }
            }
        }
        if (headerLen != offset) {
            throw new ParsingException(
                    "Actual header length " + offset + " differs from given length " + headerLen + "!");
        }
        return headerLen;
    }
    private static Pair<Field, Object> readField(Element field, int offset) {
        DataType dType = DataType.getDataType(field.getTagName());
        String name = field.getAttribute("name");
        Field f = new Field(name, dType, offset);
        String sVal = field.getTextContent();
        Object val = DataTypeParser.parseDataType(dType, sVal);
        Pair<Field, Object> pair = new Pair<>(f, val);
        return pair;
    }
    private static Msg createMsg(HashMap<Field, Object> values, int length, int headerLength, Msg.Direction dir)
            throws FieldException {
        Msg msg = new Msg(headerLength, new byte[length], length, dir);
        for (Entry<Field, Object> e : values.entrySet()) {
            Field f = e.getKey();
            f.set(msg.getData(), e.getValue());
            if (f.getName() != null && !f.getName().equals("")) {
                msg.addField(f);
            }
        }
        return msg;
    }
}
