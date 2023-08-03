
package org.openhab.binding.avmfritz.internal.hardware.callbacks;
import static org.eclipse.jetty.http.HttpMethod.GET;
import java.io.StringReader;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.openhab.binding.avmfritz.internal.dto.DeviceListModel;
import org.openhab.binding.avmfritz.internal.handler.AVMFritzBaseBridgeHandler;
import org.openhab.binding.avmfritz.internal.hardware.FritzAhaWebInterface;
import org.openhab.binding.avmfritz.internal.util.JAXBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@NonNullByDefault
public class FritzAhaUpdateCallback extends FritzAhaReauthCallback {
    private final Logger logger = LoggerFactory.getLogger(FritzAhaUpdateCallback.class);
    private static final String WEBSERVICE_COMMAND = "switchcmd=getdevicelistinfos";
    private final AVMFritzBaseBridgeHandler handler;
    public FritzAhaUpdateCallback(FritzAhaWebInterface webIface, AVMFritzBaseBridgeHandler handler) {
        super(WEBSERVICE_PATH, WEBSERVICE_COMMAND, webIface, GET, 1);
        this.handler = handler;
    }
    @Override
    public void execute(int status, String response) {
        super.execute(status, response);
        logger.trace("Received State response {}", response);
        if (isValidRequest()) {
            try {
                XMLStreamReader xsr = JAXBUtils.XMLINPUTFACTORY.createXMLStreamReader(new StringReader(response));
                Unmarshaller unmarshaller = JAXBUtils.JAXBCONTEXT_DEVICES.createUnmarshaller();
                DeviceListModel model = (DeviceListModel) unmarshaller.unmarshal(xsr);
                if (model != null) {
                    handler.onDeviceListAdded(model.getDevicelist());
                } else {
                    logger.debug("no model in response");
                }
                handler.setStatusInfo(ThingStatus.ONLINE, ThingStatusDetail.NONE, null);
            } catch (JAXBException | XMLStreamException e) {
                logger.error("Exception creating Unmarshaller: {}", e.getLocalizedMessage(), e);
                handler.setStatusInfo(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        e.getLocalizedMessage());
            }
        } else {
            logger.debug("request is invalid: {}", status);
            handler.setStatusInfo(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Request is invalid");
        }
    }
}
