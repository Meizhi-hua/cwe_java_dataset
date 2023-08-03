
package org.openhab.binding.samsungtv.internal.service;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
@NonNullByDefault
public class SamsungTvUtils {
    public static HashMap<String, String> buildHashMap(String... data) {
        HashMap<String, String> result = new HashMap<>();
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("Odd number of arguments");
        }
        String key = null;
        Integer step = -1;
        for (String value : data) {
            step++;
            switch (step % 2) {
                case 0:
                    if (value == null) {
                        throw new IllegalArgumentException("Null key value");
                    }
                    key = value;
                    continue;
                case 1:
                    if (key != null) {
                        result.put(key, value);
                    }
                    break;
            }
        }
        return result;
    }
    public static @Nullable Document loadXMLFromString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return builder.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
        }
        return null;
    }
}
