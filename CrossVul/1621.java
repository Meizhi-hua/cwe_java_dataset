
package org.openhab.binding.dlinksmarthome.internal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
public abstract class DLinkHNAPCommunication {
    private static final String LOGIN_ACTION = "\"http:
    private static final String LOGIN = "LOGIN";
    private static final String ACTION = "Action";
    private static final String USERNAME = "Username";
    private static final String LOGINPASSWORD = "LoginPassword";
    private static final String CAPTCHA = "Captcha";
    private static final String ADMIN = "Admin";
    private static final String LOGINRESULT = "LOGINResult";
    private static final String COOKIE = "Cookie";
    protected static final String HNAP_XMLNS = "http:
    protected static final String SOAPACTION = "SOAPAction";
    protected static final String OK = "OK";
    private final Logger logger = LoggerFactory.getLogger(DLinkHNAPCommunication.class);
    private URI uri;
    private final HttpClient httpClient;
    private final String pin;
    private String privateKey;
    private DocumentBuilder parser;
    private SOAPMessage requestAction;
    private SOAPMessage loginAction;
    private HNAPStatus status = HNAPStatus.INITIALISED;
    protected enum HNAPStatus {
        INITIALISED,
        LOGGED_IN,
        COMMUNICATION_ERROR,
        INTERNAL_ERROR,
        UNSUPPORTED_FIRMWARE,
        INVALID_PIN
    }
    public DLinkHNAPCommunication(final String ipAddress, final String pin) {
        this.pin = pin;
        httpClient = new HttpClient();
        try {
            uri = new URI("http:
            httpClient.start();
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final MessageFactory messageFactory = MessageFactory.newInstance();
            requestAction = messageFactory.createMessage();
            loginAction = messageFactory.createMessage();
            buildRequestAction();
            buildLoginAction();
        } catch (final SOAPException e) {
            logger.debug("DLinkHNAPCommunication - Internal error", e);
            status = HNAPStatus.INTERNAL_ERROR;
        } catch (final URISyntaxException e) {
            logger.debug("DLinkHNAPCommunication - Internal error", e);
            status = HNAPStatus.INTERNAL_ERROR;
        } catch (final ParserConfigurationException e) {
            logger.debug("DLinkHNAPCommunication - Internal error", e);
            status = HNAPStatus.INTERNAL_ERROR;
        } catch (final Exception e) {
            logger.debug("DLinkHNAPCommunication - Internal error", e);
            status = HNAPStatus.INTERNAL_ERROR;
        }
    }
    public void dispose() {
        try {
            httpClient.stop();
        } catch (final Exception e) {
        }
    }
    private void buildRequestAction() throws SOAPException {
        requestAction.getSOAPHeader().detachNode();
        final SOAPBody soapBody = requestAction.getSOAPBody();
        final SOAPElement soapBodyElem = soapBody.addChildElement(LOGIN, "", HNAP_XMLNS);
        soapBodyElem.addChildElement(ACTION).addTextNode("request");
        soapBodyElem.addChildElement(USERNAME).addTextNode(ADMIN);
        soapBodyElem.addChildElement(LOGINPASSWORD);
        soapBodyElem.addChildElement(CAPTCHA);
        final MimeHeaders headers = requestAction.getMimeHeaders();
        headers.addHeader(SOAPACTION, LOGIN_ACTION);
        requestAction.saveChanges();
    }
    private void buildLoginAction() throws SOAPException {
        loginAction.getSOAPHeader().detachNode();
        final SOAPBody soapBody = loginAction.getSOAPBody();
        final SOAPElement soapBodyElem = soapBody.addChildElement(LOGIN, "", HNAP_XMLNS);
        soapBodyElem.addChildElement(ACTION).addTextNode("login");
        soapBodyElem.addChildElement(USERNAME).addTextNode(ADMIN);
        soapBodyElem.addChildElement(LOGINPASSWORD);
        soapBodyElem.addChildElement(CAPTCHA);
        final MimeHeaders headers = loginAction.getMimeHeaders();
        headers.addHeader(SOAPACTION, LOGIN_ACTION);
    }
    private void setAuthenticationData(final String challenge, final String cookie, final String publicKey)
            throws SOAPException, InvalidKeyException, NoSuchAlgorithmException {
        final MimeHeaders loginHeaders = loginAction.getMimeHeaders();
        loginHeaders.setHeader(COOKIE, "uid=" + cookie);
        privateKey = hash(challenge, publicKey + pin);
        final String password = hash(challenge, privateKey);
        loginAction.getSOAPBody().getElementsByTagName(LOGINPASSWORD).item(0).setTextContent(password);
        loginAction.saveChanges();
    }
    private String hash(final String data, final String key) throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac mac = Mac.getInstance("HMACMD5");
        final SecretKeySpec sKey = new SecretKeySpec(key.getBytes(), "ASCII");
        mac.init(sKey);
        final byte[] bytes = mac.doFinal(data.getBytes());
        final StringBuilder hashBuf = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            final String hex = Integer.toHexString(0xFF & bytes[i]).toUpperCase();
            if (hex.length() == 1) {
                hashBuf.append('0');
            }
            hashBuf.append(hex);
        }
        return hashBuf.toString();
    }
    private void unexpectedResult(final String message, final Document soapResponse) {
        logUnexpectedResult(message, soapResponse);
        status = HNAPStatus.UNSUPPORTED_FIRMWARE;
    }
    protected HNAPStatus getHNAPStatus() {
        return status;
    }
    protected void login(final int timeout) {
        if (status != HNAPStatus.INTERNAL_ERROR) {
            try {
                Document soapResponse = sendReceive(requestAction, timeout);
                Node result = soapResponse.getElementsByTagName(LOGINRESULT).item(0);
                if (result != null && OK.equals(result.getTextContent())) {
                    final Node challengeNode = soapResponse.getElementsByTagName("Challenge").item(0);
                    final Node cookieNode = soapResponse.getElementsByTagName(COOKIE).item(0);
                    final Node publicKeyNode = soapResponse.getElementsByTagName("PublicKey").item(0);
                    if (challengeNode != null && cookieNode != null && publicKeyNode != null) {
                        setAuthenticationData(challengeNode.getTextContent(), cookieNode.getTextContent(),
                                publicKeyNode.getTextContent());
                        soapResponse = sendReceive(loginAction, timeout);
                        result = soapResponse.getElementsByTagName(LOGINRESULT).item(0);
                        if (result != null) {
                            if ("success".equals(result.getTextContent())) {
                                status = HNAPStatus.LOGGED_IN;
                            } else {
                                logger.debug("login - Check pin is correct");
                                status = HNAPStatus.INVALID_PIN;
                            }
                        } else {
                            unexpectedResult("login - Unexpected login response", soapResponse);
                        }
                    } else {
                        unexpectedResult("login - Unexpected request response", soapResponse);
                    }
                } else {
                    unexpectedResult("login - Unexpected request response", soapResponse);
                }
            } catch (final InvalidKeyException e) {
                logger.debug("login - Internal error", e);
                status = HNAPStatus.INTERNAL_ERROR;
            } catch (final NoSuchAlgorithmException e) {
                logger.debug("login - Internal error", e);
                status = HNAPStatus.INTERNAL_ERROR;
            } catch (final Exception e) {
                if (status != HNAPStatus.COMMUNICATION_ERROR) {
                    logger.debug("login - Communication error", e);
                    status = HNAPStatus.COMMUNICATION_ERROR;
                }
            }
        }
    }
    protected void setAuthenticationHeaders(final SOAPMessage action) {
        if (status == HNAPStatus.LOGGED_IN) {
            try {
                final MimeHeaders loginHeaders = loginAction.getMimeHeaders();
                final MimeHeaders actionHeaders = action.getMimeHeaders();
                actionHeaders.setHeader(COOKIE, loginHeaders.getHeader(COOKIE)[0]);
                final String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
                final String auth = hash(timeStamp + actionHeaders.getHeader(SOAPACTION)[0], privateKey) + " "
                        + timeStamp;
                actionHeaders.setHeader("HNAP_AUTH", auth);
                action.saveChanges();
            } catch (final InvalidKeyException e) {
                logger.debug("setAuthenticationHeaders - Internal error", e);
                status = HNAPStatus.INTERNAL_ERROR;
            } catch (final NoSuchAlgorithmException e) {
                logger.debug("setAuthenticationHeaders - Internal error", e);
                status = HNAPStatus.INTERNAL_ERROR;
            } catch (final SOAPException e) {
                logger.debug("setAuthenticationHeaders - Internal error", e);
                status = HNAPStatus.INTERNAL_ERROR;
            }
        }
    }
    protected Document sendReceive(final SOAPMessage action, final int timeout) throws IOException, SOAPException,
            SAXException, InterruptedException, TimeoutException, ExecutionException {
        Document result;
        final Request request = httpClient.POST(uri);
        request.timeout(timeout, TimeUnit.MILLISECONDS);
        final Iterator<?> it = action.getMimeHeaders().getAllHeaders();
        while (it.hasNext()) {
            final MimeHeader header = (MimeHeader) it.next();
            request.header(header.getName(), header.getValue());
        }
        try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            action.writeTo(os);
            request.content(new BytesContentProvider(os.toByteArray()));
            final ContentResponse response = request.send();
            try (final ByteArrayInputStream is = new ByteArrayInputStream(response.getContent())) {
                result = parser.parse(is);
            }
        }
        return result;
    }
    protected void logUnexpectedResult(final String message, final Document soapResponse) {
        if (logger.isDebugEnabled()) {
            try {
                final TransformerFactory transFactory = TransformerFactory.newInstance();
                final Transformer transformer = transFactory.newTransformer();
                final StringWriter buffer = new StringWriter();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.transform(new DOMSource(soapResponse), new StreamResult(buffer));
                logger.debug("{} : {}", message, buffer);
            } catch (final TransformerException e) {
                logger.debug("{}", message);
            }
        }
    }
}
