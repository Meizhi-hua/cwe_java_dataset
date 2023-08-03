
package org.openhab.binding.avmfritz.internal.util;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.avmfritz.internal.dto.DeviceListModel;
import org.openhab.binding.avmfritz.internal.dto.templates.TemplateListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@NonNullByDefault
public class JAXBUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JAXBUtils.class);
    public static final @Nullable JAXBContext JAXBCONTEXT_DEVICES = initJAXBContextDevices();
    public static final @Nullable JAXBContext JAXBCONTEXT_TEMPLATES = initJAXBContextTemplates();
    public static final XMLInputFactory XMLINPUTFACTORY = initXMLInputFactory();
    private static @Nullable JAXBContext initJAXBContextDevices() {
        try {
            return JAXBContext.newInstance(DeviceListModel.class);
        } catch (JAXBException e) {
            LOGGER.error("Exception creating JAXBContext for devices: {}", e.getLocalizedMessage(), e);
            return null;
        }
    }
    private static @Nullable JAXBContext initJAXBContextTemplates() {
        try {
            return JAXBContext.newInstance(TemplateListModel.class);
        } catch (JAXBException e) {
            LOGGER.error("Exception creating JAXBContext for templates: {}", e.getLocalizedMessage(), e);
            return null;
        }
    }
    private static XMLInputFactory initXMLInputFactory() {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        return xif;
    }
}
