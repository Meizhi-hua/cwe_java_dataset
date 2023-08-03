
package org.openhab.binding.fmiweather.internal.client;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.io.net.http.HttpUtil;
import org.openhab.binding.fmiweather.internal.client.FMIResponse.Builder;
import org.openhab.binding.fmiweather.internal.client.exception.FMIExceptionReportException;
import org.openhab.binding.fmiweather.internal.client.exception.FMIIOException;
import org.openhab.binding.fmiweather.internal.client.exception.FMIUnexpectedResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
@NonNullByDefault
public class Client {
    private final Logger logger = LoggerFactory.getLogger(Client.class);
    public static final String WEATHER_STATIONS_URL = "https:
    private static final Map<String, String> NAMESPACES = new HashMap<>();
    static {
        NAMESPACES.put("target", "http:
        NAMESPACES.put("gml", "http:
        NAMESPACES.put("xlink", "http:
        NAMESPACES.put("ows", "http:
        NAMESPACES.put("gmlcov", "http:
        NAMESPACES.put("swe", "http:
        NAMESPACES.put("wfs", "http:
        NAMESPACES.put("ef", "http:
    }
    private static final NamespaceContext NAMESPACE_CONTEXT = new NamespaceContext() {
        @Override
        public String getNamespaceURI(@Nullable String prefix) {
            return NAMESPACES.get(prefix);
        }
        @SuppressWarnings("rawtypes")
        @Override
        public @Nullable Iterator getPrefixes(@Nullable String val) {
            return null;
        }
        @Override
        public @Nullable String getPrefix(@Nullable String uri) {
            return null;
        }
    };
    private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder documentBuilder;
    public Client() {
        documentBuilderFactory.setNamespaceAware(true);
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
    public FMIResponse query(Request request, int timeoutMillis)
            throws FMIExceptionReportException, FMIUnexpectedResponseException, FMIIOException {
        try {
            String url = request.toUrl();
            String responseText = HttpUtil.executeUrl("GET", url, timeoutMillis);
            if (responseText == null) {
                throw new FMIIOException(String.format("HTTP error with %s", request.toUrl()));
            }
            FMIResponse response = parseMultiPointCoverageXml(responseText);
            logger.debug("Request {} translated to url {}. Response: {}", request, url, response);
            return response;
        } catch (IOException e) {
            throw new FMIIOException(e);
        } catch (SAXException | XPathExpressionException e) {
            throw new FMIUnexpectedResponseException(e);
        }
    }
    public Set<Location> queryWeatherStations(int timeoutMillis)
            throws FMIIOException, FMIUnexpectedResponseException, FMIExceptionReportException {
        try {
            String response = HttpUtil.executeUrl("GET", WEATHER_STATIONS_URL, timeoutMillis);
            if (response == null) {
                throw new FMIIOException(String.format("HTTP error with %s", WEATHER_STATIONS_URL));
            }
            return parseStations(response);
        } catch (IOException e) {
            throw new FMIIOException(e);
        } catch (XPathExpressionException | SAXException e) {
            throw new FMIUnexpectedResponseException(e);
        }
    }
    private Set<Location> parseStations(String response) throws FMIExceptionReportException,
            FMIUnexpectedResponseException, SAXException, IOException, XPathExpressionException {
        Document document = documentBuilder.parse(new InputSource(new StringReader(response)));
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(NAMESPACE_CONTEXT);
        boolean isExceptionReport = ((Node) xPath.compile("/ows:ExceptionReport").evaluate(document,
                XPathConstants.NODE)) != null;
        if (isExceptionReport) {
            Node exceptionCode = (Node) xPath.compile("/ows:ExceptionReport/ows:Exception/@exceptionCode")
                    .evaluate(document, XPathConstants.NODE);
            String[] exceptionText = queryNodeValues(xPath.compile("
            throw new FMIExceptionReportException(exceptionCode.getNodeValue(), exceptionText);
        }
        String[] fmisids = queryNodeValues(
                xPath.compile(
                        "/wfs:FeatureCollection/wfs:member/ef:EnvironmentalMonitoringFacility/gml:identifier/text()"),
                document);
        String[] names = queryNodeValues(xPath.compile(
                "/wfs:FeatureCollection/wfs:member/ef:EnvironmentalMonitoringFacility/gml:name[@codeSpace='http:
                document);
        String[] representativePoints = queryNodeValues(xPath.compile(
                "/wfs:FeatureCollection/wfs:member/ef:EnvironmentalMonitoringFacility/ef:representativePoint/gml:Point/gml:pos/text()"),
                document);
        if (fmisids.length != names.length || fmisids.length != representativePoints.length) {
            throw new FMIUnexpectedResponseException(String.format(
                    "Could not all properties of locations: fmisids: %d, names: %d, representativePoints: %d",
                    fmisids.length, names.length, representativePoints.length));
        }
        Set<Location> locations = new HashSet<>(representativePoints.length);
        for (int i = 0; i < representativePoints.length; i++) {
            BigDecimal[] latlon = parseLatLon(representativePoints[i]);
            locations.add(new Location(names[i], fmisids[i], latlon[0], latlon[1]));
        }
        return locations;
    }
    private FMIResponse parseMultiPointCoverageXml(String response) throws FMIUnexpectedResponseException,
            FMIExceptionReportException, SAXException, IOException, XPathExpressionException {
        Document document = documentBuilder.parse(new InputSource(new StringReader(response)));
        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(NAMESPACE_CONTEXT);
        boolean isExceptionReport = ((Node) xPath.compile("/ows:ExceptionReport").evaluate(document,
                XPathConstants.NODE)) != null;
        if (isExceptionReport) {
            Node exceptionCode = (Node) xPath.compile("/ows:ExceptionReport/ows:Exception/@exceptionCode")
                    .evaluate(document, XPathConstants.NODE);
            String[] exceptionText = queryNodeValues(xPath.compile("
            throw new FMIExceptionReportException(exceptionCode.getNodeValue(), exceptionText);
        }
        Builder builder = new FMIResponse.Builder();
        String[] parameters = queryNodeValues(xPath.compile("
        String[] ids = queryNodeValues(xPath.compile(
                "
                document);
        String[] names = queryNodeValues(xPath.compile(
                "
                document);
        String[] representativePointRefs = queryNodeValues(
                xPath.compile("
        if ((ids.length > 0 && ids.length != names.length) || names.length != representativePointRefs.length) {
            throw new FMIUnexpectedResponseException(String.format(
                    "Could not all properties of locations: ids: %d, names: %d, representativePointRefs: %d",
                    ids.length, names.length, representativePointRefs.length));
        }
        Location[] locations = new Location[representativePointRefs.length];
        for (int i = 0; i < locations.length; i++) {
            BigDecimal[] latlon = findLatLon(xPath, i, document, representativePointRefs[i]);
            String id = ids.length == 0 ? String.format("%s,%s", latlon[0].toPlainString(), latlon[1].toPlainString())
                    : ids[i];
            locations[i] = new Location(names[i], id, latlon[0], latlon[1]);
        }
        logger.trace("names ({}): {}", names.length, names);
        logger.trace("parameters ({}): {}", parameters.length, parameters);
        if (names.length == 0) {
            return builder.build();
        }
        String latLonTimeTripletText = takeFirstOrError("positions",
                queryNodeValues(xPath.compile("
        String[] latLonTimeTripletEntries = latLonTimeTripletText.trim().split("\\s+");
        logger.trace("latLonTimeTripletText: {}", latLonTimeTripletText);
        logger.trace("latLonTimeTripletEntries ({}): {}", latLonTimeTripletEntries.length, latLonTimeTripletEntries);
        int countTimestamps = latLonTimeTripletEntries.length / 3 / locations.length;
        long[] timestampsEpoch = IntStream.range(0, latLonTimeTripletEntries.length).filter(i -> i % 3 == 0)
                .limit(countTimestamps).mapToLong(i -> Long.parseLong(latLonTimeTripletEntries[i + 2])).toArray();
        assert countTimestamps == timestampsEpoch.length;
        logger.trace("countTimestamps ({}): {}", countTimestamps, timestampsEpoch);
        validatePositionEntries(locations, timestampsEpoch, latLonTimeTripletEntries);
        String valuesText = takeFirstOrError("doubleOrNilReasonTupleList",
                queryNodeValues(xPath.compile(".
        String[] valuesEntries = valuesText.trim().split("\\s+");
        logger.trace("valuesText: {}", valuesText);
        logger.trace("valuesEntries ({}): {}", valuesEntries.length, valuesEntries);
        if (valuesEntries.length != locations.length * parameters.length * countTimestamps) {
            throw new FMIUnexpectedResponseException(String.format(
                    "Wrong number of values (%d). Expecting %d * %d * %d = %d", valuesEntries.length, locations.length,
                    parameters.length, countTimestamps, countTimestamps * locations.length * parameters.length));
        }
        IntStream.range(0, locations.length).forEach(locationIndex -> {
            for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
                for (int timestepIndex = 0; timestepIndex < countTimestamps; timestepIndex++) {
                    BigDecimal val = toBigDecimalOrNullIfNaN(
                            valuesEntries[locationIndex * countTimestamps * parameters.length
                                    + timestepIndex * parameters.length + parameterIndex]);
                    logger.trace("Found value {}={} @ time={} for location {}", parameters[parameterIndex], val,
                            timestampsEpoch[timestepIndex], locations[locationIndex].id);
                    builder.appendLocationData(locations[locationIndex], countTimestamps, parameters[parameterIndex],
                            timestampsEpoch[timestepIndex], val);
                }
            }
        });
        return builder.build();
    }
    private BigDecimal[] findLatLon(XPath xPath, int entryIndex, Document document, String href)
            throws FMIUnexpectedResponseException, XPathExpressionException {
        if (!href.startsWith("#")) {
            throw new FMIUnexpectedResponseException(
                    "Could not find valid representativePoint xlink:href, does not start with #");
        }
        String pointId = href.substring(1);
        String pointLatLon = takeFirstOrError(String.format("[%d]/pos", entryIndex),
                queryNodeValues(xPath.compile(".
        return parseLatLon(pointLatLon);
    }
    private BigDecimal[] parseLatLon(String pointLatLon) throws FMIUnexpectedResponseException {
        String[] latlon = pointLatLon.split(" ");
        BigDecimal lat, lon;
        if (latlon.length != 2) {
            throw new FMIUnexpectedResponseException(String.format(
                    "Invalid latitude or longitude format, expected two values separated by space, got %d values: '%s'",
                    latlon.length, latlon));
        }
        try {
            lat = new BigDecimal(latlon[0]);
            lon = new BigDecimal(latlon[1]);
        } catch (NumberFormatException e) {
            throw new FMIUnexpectedResponseException(
                    String.format("Invalid latitude or longitude format: %s", e.getMessage()));
        }
        return new BigDecimal[] { lat, lon };
    }
    private String[] queryNodeValues(XPathExpression expression, Object source) throws XPathExpressionException {
        NodeList nodeList = (NodeList) expression.evaluate(source, XPathConstants.NODESET);
        String[] values = new String[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            values[i] = nodeList.item(i).getNodeValue();
        }
        return values;
    }
    private String takeFirstOrError(String errorDescription, String[] values) throws FMIUnexpectedResponseException {
        if (values.length != 1) {
            throw new FMIUnexpectedResponseException(String.format("No unique match found: %s", errorDescription));
        }
        return values[0];
    }
    private @Nullable BigDecimal toBigDecimalOrNullIfNaN(String value) {
        if ("NaN".equals(value)) {
            return null;
        } else {
            return new BigDecimal(value);
        }
    }
    private void validatePositionEntries(Location[] locations, long[] timestampsEpoch,
            String[] latLonTimeTripletEntries) throws FMIUnexpectedResponseException {
        int countTimestamps = timestampsEpoch.length;
        for (int locationIndex = 0; locationIndex < locations.length; locationIndex++) {
            String firstLat = latLonTimeTripletEntries[locationIndex * countTimestamps * 3];
            String fistLon = latLonTimeTripletEntries[locationIndex * countTimestamps * 3 + 1];
            for (int timestepIndex = 0; timestepIndex < countTimestamps; timestepIndex++) {
                String lat = latLonTimeTripletEntries[locationIndex * countTimestamps * 3 + timestepIndex * 3];
                String lon = latLonTimeTripletEntries[locationIndex * countTimestamps * 3 + timestepIndex * 3 + 1];
                String timeEpochSec = latLonTimeTripletEntries[locationIndex * countTimestamps * 3 + timestepIndex * 3
                        + 2];
                if (!lat.equals(firstLat) || !lon.equals(fistLon)) {
                    throw new FMIUnexpectedResponseException(String.format(
                            "positions[%d] lat, lon for time index [%d] was not matching expected ordering",
                            locationIndex, timestepIndex));
                }
                String expectedLat = locations[locationIndex].latitude.toPlainString();
                String expectedLon = locations[locationIndex].longitude.toPlainString();
                if (!lat.equals(expectedLat) || !lon.equals(expectedLon)) {
                    throw new FMIUnexpectedResponseException(String.format(
                            "positions[%d] lat, lon for time index [%d] was not matching representativePoint",
                            locationIndex, timestepIndex));
                }
                if (Long.parseLong(timeEpochSec) != timestampsEpoch[timestepIndex]) {
                    throw new FMIUnexpectedResponseException(String.format(
                            "positions[%d] time (%s) for time index [%d] was not matching expected (%d) ordering",
                            locationIndex, timeEpochSec, timestepIndex, timestampsEpoch[timestepIndex]));
                }
            }
        }
    }
}
