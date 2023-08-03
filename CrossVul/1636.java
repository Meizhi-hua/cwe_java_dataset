package org.mapfish.print.map.style;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.DefaultResourceLocator;
import org.geotools.styling.Style;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.util.Assert;
import org.mapfish.print.Constants;
import org.mapfish.print.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
public class SLDParserPlugin implements StyleParserPlugin {
    public static final String STYLE_INDEX_REF_SEPARATOR = "##";
    @Override
    public final Optional<Style> parseStyle(
            @Nullable final Configuration configuration,
            @Nonnull final ClientHttpRequestFactory clientHttpRequestFactory,
            @Nonnull final String styleString) {
        final Optional<Style> styleOptional = tryLoadSLD(
                styleString.getBytes(Constants.DEFAULT_CHARSET), null, clientHttpRequestFactory);
        if (styleOptional.isPresent()) {
            return styleOptional;
        }
        final Integer styleIndex = lookupStyleIndex(styleString).orElse(null);
        final String styleStringWithoutIndexReference = removeIndexReference(styleString);
        Function<byte[], Optional<Style>> loadFunction =
                input -> tryLoadSLD(input, styleIndex, clientHttpRequestFactory);
        return ParserPluginUtils.loadStyleAsURI(clientHttpRequestFactory, styleStringWithoutIndexReference,
                                                loadFunction);
    }
    private String removeIndexReference(final String styleString) {
        int styleIdentifier = styleString.lastIndexOf(STYLE_INDEX_REF_SEPARATOR);
        if (styleIdentifier > 0) {
            return styleString.substring(0, styleIdentifier);
        }
        return styleString;
    }
    private Optional<Integer> lookupStyleIndex(final String ref) {
        int styleIdentifier = ref.lastIndexOf(STYLE_INDEX_REF_SEPARATOR);
        if (styleIdentifier > 0) {
            return Optional.of(Integer.parseInt(ref.substring(styleIdentifier + 2)) - 1);
        }
        return Optional.empty();
    }
    private Optional<Style> tryLoadSLD(
            final byte[] bytes, final Integer styleIndex,
            final ClientHttpRequestFactory clientHttpRequestFactory) {
        Assert.isTrue(styleIndex == null || styleIndex > -1,
                      "styleIndex must be > -1 but was: " + styleIndex);
        final Style[] styles;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler());
            db.parse(new ByteArrayInputStream(bytes));
            final SLDParser sldParser = new SLDParser(CommonFactoryFinder.getStyleFactory());
            sldParser.setOnLineResourceLocator(new DefaultResourceLocator() {
                @Override
                public URL locateResource(final String uri) {
                    try {
                        final URL theUrl = super.locateResource(uri);
                        final URI theUri;
                        if (theUrl != null) {
                            theUri = theUrl.toURI();
                        } else {
                            theUri = URI.create(uri);
                        }
                        if (theUri.getScheme().startsWith("http")) {
                            final ClientHttpRequest request = clientHttpRequestFactory.createRequest(
                                    theUri, HttpMethod.GET);
                            return request.getURI().toURL();
                        }
                        return null;
                    } catch (IOException | URISyntaxException e) {
                        return null;
                    }
                }
            });
            sldParser.setInput(new ByteArrayInputStream(bytes));
            styles = sldParser.readXML();
        } catch (Throwable e) {
            return Optional.empty();
        }
        if (styleIndex != null) {
            Assert.isTrue(styleIndex < styles.length, String.format("There where %s styles in file but " +
                                                                            "requested index was: %s",
                                                                    styles.length, styleIndex + 1));
        } else {
            Assert.isTrue(styles.length < 2, String.format("There are %s therefore the styleRef must " +
                                                                   "contain an index identifying the style." +
                                                                   "  The index starts at 1 for the first " +
                                                                   "style.\n" +
                                                                   "\tExample: thinline.sld##1",
                                                           styles.length));
        }
        if (styleIndex == null) {
            return Optional.of(styles[0]);
        } else {
            return Optional.of(styles[styleIndex]);
        }
    }
    public static class ErrorHandler extends DefaultHandler {
        private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);
        public final void error(final SAXParseException e) throws SAXException {
            LOGGER.debug("XML error: {}", e.getLocalizedMessage());
            super.error(e);
        }
        public final void fatalError(final SAXParseException e) throws SAXException {
            LOGGER.debug("XML fatal error: {}", e.getLocalizedMessage());
            super.fatalError(e);
        }
        public final void warning(final SAXParseException e) {
        }
    }
}
