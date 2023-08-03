
package org.openhab.binding.hpprinter.internal.api;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.hpprinter.internal.api.HPServerResult.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
@NonNullByDefault
public class HPWebServerClient {
    public static final int REQUEST_TIMEOUT_SEC = 10;
    private final Logger logger = LoggerFactory.getLogger(HPWebServerClient.class);
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private final HttpClient httpClient;
    private final String serverAddress;
    public HPWebServerClient(HttpClient httpClient, String address) {
        this.httpClient = httpClient;
        serverAddress = "http:
        logger.debug("Create printer connection {}", serverAddress);
    }
    public HPServerResult<HPStatus> getStatus() {
        return fetchData(serverAddress + HPStatus.ENDPOINT, (HPStatus::new));
    }
    public HPServerResult<HPProductUsageFeatures> getProductFeatures() {
        return fetchData(serverAddress + HPProductUsageFeatures.ENDPOINT, (HPProductUsageFeatures::new));
    }
    public HPServerResult<HPFeatures> getProductUsageFeatures() {
        return fetchData(serverAddress + HPFeatures.ENDPOINT, (HPFeatures::new));
    }
    public HPServerResult<HPScannerStatusFeatures> getScannerFeatures() {
        return fetchData(serverAddress + HPScannerStatusFeatures.ENDPOINT, (HPScannerStatusFeatures::new));
    }
    public HPServerResult<HPUsage> getUsage() {
        return fetchData(serverAddress + HPUsage.ENDPOINT, (HPUsage::new));
    }
    public HPServerResult<HPScannerStatus> getScannerStatus() {
        return fetchData(serverAddress + HPScannerStatus.ENDPOINT, (HPScannerStatus::new));
    }
    public HPServerResult<HPProperties> getProperties() {
        return fetchData(serverAddress + HPProperties.ENDPOINT, (HPProperties::new));
    }
    private <T> HPServerResult<T> fetchData(String endpoint, Function<Document, T> function) {
        try {
            logger.trace("HTTP Client Load {}", endpoint);
            ContentResponse cr = httpClient.newRequest(endpoint).method(HttpMethod.GET)
                    .timeout(REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS).send();
            String contentAsString = cr.getContentAsString();
            logger.trace("HTTP Client Result {} Size {}", cr.getStatus(), contentAsString.length());
            return new HPServerResult<>(function.apply(getDocument(contentAsString)));
        } catch (TimeoutException ex) {
            logger.trace("HTTP Client Timeout Exception {}", ex.getMessage());
            return new HPServerResult<>(RequestStatus.TIMEOUT, ex.getMessage());
        } catch (InterruptedException | ExecutionException | ParserConfigurationException | SAXException
                | IOException ex) {
            logger.trace("HTTP Client Exception {}", ex.getMessage());
            return new HPServerResult<>(RequestStatus.ERROR, ex.getMessage());
        }
    }
    private synchronized Document getDocument(String contentAsString)
            throws ParserConfigurationException, SAXException, IOException {
        factory.setFeature("http:
        factory.setFeature("http:
        factory.setFeature("http:
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource source = new InputSource(new StringReader(contentAsString));
        return builder.parse(source);
    }
}
