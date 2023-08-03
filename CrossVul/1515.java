
package com.linecorp.armeria.internal;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static io.netty.handler.codec.http.HttpUtil.isAsteriskForm;
import static io.netty.handler.codec.http.HttpUtil.isOriginForm;
import static io.netty.handler.codec.http2.Http2Error.PROTOCOL_ERROR;
import static io.netty.handler.codec.http2.Http2Exception.streamError;
import static io.netty.util.AsciiString.EMPTY_STRING;
import static io.netty.util.ByteProcessor.FIND_COMMA;
import static io.netty.util.internal.StringUtil.decodeHexNibble;
import static io.netty.util.internal.StringUtil.isNullOrEmpty;
import static io.netty.util.internal.StringUtil.length;
import static java.util.Objects.requireNonNull;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Ascii;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.linecorp.armeria.common.Flags;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpHeadersBuilder;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.common.RequestHeadersBuilder;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.ResponseHeadersBuilder;
import com.linecorp.armeria.server.ServerConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.UnsupportedValueConverter;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.HttpConversionUtil;
import io.netty.handler.codec.http2.HttpConversionUtil.ExtensionHeaderNames;
import io.netty.util.AsciiString;
import io.netty.util.HashingStrategy;
import io.netty.util.internal.StringUtil;
public final class ArmeriaHttpUtil {
    private static final HashingStrategy<AsciiString> HTTP2_HEADER_NAME_HASHER =
            new HashingStrategy<AsciiString>() {
                @Override
                public int hashCode(AsciiString o) {
                    return o.hashCode();
                }
                @Override
                public boolean equals(AsciiString a, AsciiString b) {
                    return a.contentEqualsIgnoreCase(b);
                }
            };
    public static final Charset HTTP_DEFAULT_CONTENT_CHARSET = StandardCharsets.ISO_8859_1;
    public static final AsciiString HEADER_NAME_KEEP_ALIVE = AsciiString.cached("keep-alive");
    public static final AsciiString HEADER_NAME_PROXY_CONNECTION = AsciiString.cached("proxy-connection");
    private static final URI ROOT = URI.create("/");
    private static final CharSequenceMap HTTP_TO_HTTP2_HEADER_BLACKLIST = new CharSequenceMap();
    private static final CharSequenceMap HTTP2_TO_HTTP_HEADER_BLACKLIST = new CharSequenceMap();
    private static final CharSequenceMap HTTP_TRAILER_BLACKLIST = new CharSequenceMap();
    static {
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.CONNECTION, EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HEADER_NAME_KEEP_ALIVE, EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HEADER_NAME_PROXY_CONNECTION, EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.TRANSFER_ENCODING, EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.HOST, EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(HttpHeaderNames.UPGRADE, EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.STREAM_ID.text(), EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.SCHEME.text(), EMPTY_STRING);
        HTTP_TO_HTTP2_HEADER_BLACKLIST.add(ExtensionHeaderNames.PATH.text(), EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(HttpHeaderNames.AUTHORITY, EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(HttpHeaderNames.METHOD, EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(HttpHeaderNames.PATH, EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(HttpHeaderNames.SCHEME, EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(HttpHeaderNames.STATUS, EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(HttpHeaderNames.TRANSFER_ENCODING, EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(ExtensionHeaderNames.STREAM_ID.text(), EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(ExtensionHeaderNames.SCHEME.text(), EMPTY_STRING);
        HTTP2_TO_HTTP_HEADER_BLACKLIST.add(ExtensionHeaderNames.PATH.text(), EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.TRANSFER_ENCODING, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.CONTENT_LENGTH, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.CACHE_CONTROL, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.EXPECT, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.HOST, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.MAX_FORWARDS, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.PRAGMA, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.RANGE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.TE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.WWW_AUTHENTICATE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.AUTHORIZATION, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.PROXY_AUTHENTICATE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.PROXY_AUTHORIZATION, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.DATE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.LOCATION, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.RETRY_AFTER, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.VARY, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.WARNING, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.CONTENT_ENCODING, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.CONTENT_TYPE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.CONTENT_RANGE, EMPTY_STRING);
        HTTP_TRAILER_BLACKLIST.add(HttpHeaderNames.TRAILER, EMPTY_STRING);
    }
    private static final CharSequenceMap REQUEST_HEADER_TRANSLATIONS = new CharSequenceMap();
    private static final CharSequenceMap RESPONSE_HEADER_TRANSLATIONS = new CharSequenceMap();
    static {
        RESPONSE_HEADER_TRANSLATIONS.add(Http2Headers.PseudoHeaderName.AUTHORITY.value(),
                                         HttpHeaderNames.HOST);
        REQUEST_HEADER_TRANSLATIONS.add(RESPONSE_HEADER_TRANSLATIONS);
    }
    private static final String EMPTY_REQUEST_PATH = "/";
    private static final Splitter COOKIE_SPLITTER = Splitter.on(';').trimResults().omitEmptyStrings();
    private static final String COOKIE_SEPARATOR = "; ";
    @Nullable
    private static final LoadingCache<AsciiString, String> HEADER_VALUE_CACHE =
            Flags.headerValueCacheSpec().map(ArmeriaHttpUtil::buildCache).orElse(null);
    private static final Set<AsciiString> CACHED_HEADERS = Flags.cachedHeaders().stream().map(AsciiString::of)
                                                                .collect(toImmutableSet());
    private static LoadingCache<AsciiString, String> buildCache(String spec) {
        return Caffeine.from(spec).build(AsciiString::toString);
    }
    public static String concatPaths(@Nullable String path1, @Nullable String path2) {
        path2 = path2 == null ? "" : path2;
        if (path1 == null || path1.isEmpty() || EMPTY_REQUEST_PATH.equals(path1)) {
            if (path2.isEmpty()) {
                return EMPTY_REQUEST_PATH;
            }
            if (path2.charAt(0) == '/') {
                return path2; 
            }
            return '/' + path2;
        }
        if (path2.isEmpty()) {
            return path1;
        }
        if (path1.charAt(path1.length() - 1) == '/') {
            if (path2.charAt(0) == '/') {
                return new StringBuilder(path1.length() + path2.length() - 1)
                        .append(path1).append(path2, 1, path2.length()).toString();
            }
            return path1 + path2;
        }
        if (path2.charAt(0) == '/') {
            return path1 + path2;
        }
        return path1 + '/' + path2;
    }
    public static String decodePath(String path) {
        if (path.indexOf('%') < 0) {
            return path;
        }
        final int len = path.length();
        final byte[] buf = ThreadLocalByteArray.get(len);
        int dstLen = 0;
        for (int i = 0; i < len; i++) {
            final char ch = path.charAt(i);
            if (ch != '%') {
                buf[dstLen++] = (byte) ((ch & 0xFF80) == 0 ? ch : 0xFF);
                continue;
            }
            final int hexEnd = i + 3;
            if (hexEnd > len) {
                buf[dstLen++] = (byte) 0xFF;
                break;
            }
            final int digit1 = decodeHexNibble(path.charAt(++i));
            final int digit2 = decodeHexNibble(path.charAt(++i));
            if (digit1 < 0 || digit2 < 0) {
                buf[dstLen++] = (byte) 0xFF;
            } else {
                buf[dstLen++] = (byte) ((digit1 << 4) | digit2);
            }
        }
        return new String(buf, 0, dstLen, StandardCharsets.UTF_8);
    }
    public static boolean isAbsoluteUri(@Nullable String maybeUri) {
        if (maybeUri == null) {
            return false;
        }
        final int firstColonIdx = maybeUri.indexOf(':');
        if (firstColonIdx <= 0 || firstColonIdx + 3 >= maybeUri.length()) {
            return false;
        }
        final int firstSlashIdx = maybeUri.indexOf('/');
        if (firstSlashIdx <= 0 || firstSlashIdx < firstColonIdx) {
            return false;
        }
        return maybeUri.charAt(firstColonIdx + 1) == '/' && maybeUri.charAt(firstColonIdx + 2) == '/';
    }
    public static boolean isInformational(@Nullable String statusText) {
        return statusText != null && !statusText.isEmpty() && statusText.charAt(0) == '1';
    }
    public static boolean isContentAlwaysEmptyWithValidation(
            HttpStatus status, HttpData content, HttpHeaders trailers) {
        if (!status.isContentAlwaysEmpty()) {
            return false;
        }
        if (!content.isEmpty()) {
            throw new IllegalArgumentException(
                    "A " + status + " response must have empty content: " + content.length() + " byte(s)");
        }
        if (!trailers.isEmpty()) {
            throw new IllegalArgumentException(
                    "A " + status + " response must not have trailers: " + trailers);
        }
        return true;
    }
    public static boolean isCorsPreflightRequest(com.linecorp.armeria.common.HttpRequest request) {
        requireNonNull(request, "request");
        return request.method() == HttpMethod.OPTIONS &&
               request.headers().contains(HttpHeaderNames.ORIGIN) &&
               request.headers().contains(HttpHeaderNames.ACCESS_CONTROL_REQUEST_METHOD);
    }
    public static void parseDirectives(String directives, BiConsumer<String, String> callback) {
        final int len = directives.length();
        for (int i = 0; i < len;) {
            final int nameStart = i;
            final String name;
            final String value;
            for (; i < len; i++) {
                final char ch = directives.charAt(i);
                if (ch == ',' || ch == '=') {
                    break;
                }
            }
            name = directives.substring(nameStart, i).trim();
            if (i == len || directives.charAt(i) == ',') {
                i++;
                value = null;
            } else {
                i++;
                for (; i < len; i++) {
                    final char ch = directives.charAt(i);
                    if (ch != ' ' && ch != '\t') {
                        break;
                    }
                }
                if (i < len && directives.charAt(i) == '\"') {
                    i++;
                    final int valueStart = i;
                    for (; i < len; i++) {
                        if (directives.charAt(i) == '\"') {
                            break;
                        }
                    }
                    value = directives.substring(valueStart, i);
                    i++;
                    for (; i < len; i++) {
                        if (directives.charAt(i) == ',') {
                            i++;
                            break;
                        }
                    }
                } else {
                    final int valueStart = i;
                    for (; i < len; i++) {
                        if (directives.charAt(i) == ',') {
                            break;
                        }
                    }
                    value = directives.substring(valueStart, i).trim();
                    i++;
                }
            }
            if (!name.isEmpty()) {
                callback.accept(Ascii.toLowerCase(name), Strings.emptyToNull(value));
            }
        }
    }
    public static long parseDirectiveValueAsSeconds(@Nullable String value) {
        if (value == null) {
            return -1;
        }
        try {
            final long converted = Long.parseLong(value);
            return converted >= 0 ? converted : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public static RequestHeaders toArmeriaRequestHeaders(ChannelHandlerContext ctx, Http2Headers headers,
                                                         boolean endOfStream, String scheme,
                                                         ServerConfig cfg) {
        final RequestHeadersBuilder builder = RequestHeaders.builder();
        toArmeria(builder, headers, endOfStream);
        if (!builder.contains(HttpHeaderNames.SCHEME)) {
            builder.add(HttpHeaderNames.SCHEME, scheme);
        }
        if (!builder.contains(HttpHeaderNames.AUTHORITY)) {
            final String defaultHostname = cfg.defaultVirtualHost().defaultHostname();
            final int port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();
            builder.add(HttpHeaderNames.AUTHORITY, defaultHostname + ':' + port);
        }
        return builder.build();
    }
    public static HttpHeaders toArmeria(Http2Headers headers, boolean request, boolean endOfStream) {
        final HttpHeadersBuilder builder;
        if (request) {
            builder = headers.contains(HttpHeaderNames.METHOD) ? RequestHeaders.builder()
                                                               : HttpHeaders.builder();
        } else {
            builder = headers.contains(HttpHeaderNames.STATUS) ? ResponseHeaders.builder()
                                                               : HttpHeaders.builder();
        }
        toArmeria(builder, headers, endOfStream);
        return builder.build();
    }
    private static void toArmeria(HttpHeadersBuilder builder, Http2Headers headers, boolean endOfStream) {
        builder.sizeHint(headers.size());
        builder.endOfStream(endOfStream);
        StringJoiner cookieJoiner = null;
        for (Entry<CharSequence, CharSequence> e : headers) {
            final AsciiString name = HttpHeaderNames.of(e.getKey());
            final CharSequence value = e.getValue();
            if (name.equals(HttpHeaderNames.COOKIE)) {
                if (cookieJoiner == null) {
                    cookieJoiner = new StringJoiner(COOKIE_SEPARATOR);
                }
                COOKIE_SPLITTER.split(value).forEach(cookieJoiner::add);
            } else {
                builder.add(name, convertHeaderValue(name, value));
            }
        }
        if (cookieJoiner != null && cookieJoiner.length() != 0) {
            builder.add(HttpHeaderNames.COOKIE, cookieJoiner.toString());
        }
    }
    public static RequestHeaders toArmeria(ChannelHandlerContext ctx, HttpRequest in,
                                           ServerConfig cfg) throws URISyntaxException {
        final URI requestTargetUri = toUri(in);
        final io.netty.handler.codec.http.HttpHeaders inHeaders = in.headers();
        final RequestHeadersBuilder out = RequestHeaders.builder();
        out.sizeHint(inHeaders.size());
        out.add(HttpHeaderNames.METHOD, in.method().name());
        out.add(HttpHeaderNames.PATH, toHttp2Path(requestTargetUri));
        addHttp2Scheme(inHeaders, requestTargetUri, out);
        if (!isOriginForm(requestTargetUri) && !isAsteriskForm(requestTargetUri)) {
            final String host = inHeaders.getAsString(HttpHeaderNames.HOST);
            addHttp2Authority(host == null || host.isEmpty() ? requestTargetUri.getAuthority() : host, out);
        }
        if (out.authority() == null) {
            final String defaultHostname = cfg.defaultVirtualHost().defaultHostname();
            final int port = ((InetSocketAddress) ctx.channel().localAddress()).getPort();
            out.add(HttpHeaderNames.AUTHORITY, defaultHostname + ':' + port);
        }
        toArmeria(inHeaders, out);
        return out.build();
    }
    public static ResponseHeaders toArmeria(HttpResponse in) {
        final io.netty.handler.codec.http.HttpHeaders inHeaders = in.headers();
        final ResponseHeadersBuilder out = ResponseHeaders.builder();
        out.sizeHint(inHeaders.size());
        out.add(HttpHeaderNames.STATUS, HttpStatus.valueOf(in.status().code()).codeAsText());
        toArmeria(inHeaders, out);
        return out.build();
    }
    public static HttpHeaders toArmeria(io.netty.handler.codec.http.HttpHeaders inHeaders) {
        if (inHeaders.isEmpty()) {
            return HttpHeaders.of();
        }
        final HttpHeadersBuilder out = HttpHeaders.builder();
        out.sizeHint(inHeaders.size());
        toArmeria(inHeaders, out);
        return out.build();
    }
    public static void toArmeria(io.netty.handler.codec.http.HttpHeaders inHeaders, HttpHeadersBuilder out) {
        final Iterator<Entry<CharSequence, CharSequence>> iter = inHeaders.iteratorCharSequence();
        final CharSequenceMap connectionBlacklist =
                toLowercaseMap(inHeaders.valueCharSequenceIterator(HttpHeaderNames.CONNECTION), 8);
        StringJoiner cookieJoiner = null;
        while (iter.hasNext()) {
            final Entry<CharSequence, CharSequence> entry = iter.next();
            final AsciiString aName = HttpHeaderNames.of(entry.getKey()).toLowerCase();
            if (HTTP_TO_HTTP2_HEADER_BLACKLIST.contains(aName) || connectionBlacklist.contains(aName)) {
                continue;
            }
            if (aName.equals(HttpHeaderNames.TE)) {
                toHttp2HeadersFilterTE(entry, out);
                continue;
            }
            final CharSequence value = entry.getValue();
            if (aName.equals(HttpHeaderNames.COOKIE)) {
                if (cookieJoiner == null) {
                    cookieJoiner = new StringJoiner(COOKIE_SEPARATOR);
                }
                COOKIE_SPLITTER.split(value).forEach(cookieJoiner::add);
            } else {
                out.add(aName, convertHeaderValue(aName, value));
            }
        }
        if (cookieJoiner != null && cookieJoiner.length() != 0) {
            out.add(HttpHeaderNames.COOKIE, cookieJoiner.toString());
        }
    }
    private static CharSequenceMap toLowercaseMap(Iterator<? extends CharSequence> valuesIter,
                                                  int arraySizeHint) {
        final CharSequenceMap result = new CharSequenceMap(arraySizeHint);
        while (valuesIter.hasNext()) {
            final AsciiString lowerCased = HttpHeaderNames.of(valuesIter.next()).toLowerCase();
            try {
                int index = lowerCased.forEachByte(FIND_COMMA);
                if (index != -1) {
                    int start = 0;
                    do {
                        result.add(lowerCased.subSequence(start, index, false).trim(), EMPTY_STRING);
                        start = index + 1;
                    } while (start < lowerCased.length() &&
                             (index = lowerCased.forEachByte(start,
                                                             lowerCased.length() - start, FIND_COMMA)) != -1);
                    result.add(lowerCased.subSequence(start, lowerCased.length(), false).trim(), EMPTY_STRING);
                } else {
                    result.add(lowerCased.trim(), EMPTY_STRING);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }
    private static void toHttp2HeadersFilterTE(Entry<CharSequence, CharSequence> entry,
                                               HttpHeadersBuilder out) {
        if (AsciiString.indexOf(entry.getValue(), ',', 0) == -1) {
            if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(entry.getValue()),
                                                    HttpHeaderValues.TRAILERS)) {
                out.add(HttpHeaderNames.TE, HttpHeaderValues.TRAILERS.toString());
            }
        } else {
            final List<CharSequence> teValues = StringUtil.unescapeCsvFields(entry.getValue());
            for (CharSequence teValue : teValues) {
                if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(teValue),
                                                        HttpHeaderValues.TRAILERS)) {
                    out.add(HttpHeaderNames.TE, HttpHeaderValues.TRAILERS.toString());
                    break;
                }
            }
        }
    }
    private static URI toUri(HttpRequest in) throws URISyntaxException {
        final String uri = in.uri();
        if (uri.startsWith("
            for (int i = 0; i < uri.length(); i++) {
                if (uri.charAt(i) != '/') {
                    return new URI(uri.substring(i - 1));
                }
            }
            return ROOT;
        } else {
            return new URI(uri);
        }
    }
    private static String toHttp2Path(URI uri) {
        final StringBuilder pathBuilder = new StringBuilder(
                length(uri.getRawPath()) + length(uri.getRawQuery()) + length(uri.getRawFragment()) + 2);
        if (!isNullOrEmpty(uri.getRawPath())) {
            pathBuilder.append(uri.getRawPath());
        }
        if (!isNullOrEmpty(uri.getRawQuery())) {
            pathBuilder.append('?');
            pathBuilder.append(uri.getRawQuery());
        }
        if (!isNullOrEmpty(uri.getRawFragment())) {
            pathBuilder.append('#');
            pathBuilder.append(uri.getRawFragment());
        }
        return pathBuilder.length() != 0 ? pathBuilder.toString() : EMPTY_REQUEST_PATH;
    }
    @VisibleForTesting
    static void addHttp2Authority(@Nullable String authority, RequestHeadersBuilder out) {
        if (authority != null) {
            final String actualAuthority;
            if (authority.isEmpty()) {
                actualAuthority = "";
            } else {
                final int start = authority.indexOf('@') + 1;
                if (start == 0) {
                    actualAuthority = authority;
                } else if (authority.length() == start) {
                    throw new IllegalArgumentException("authority: " + authority);
                } else {
                    actualAuthority = authority.substring(start);
                }
            }
            out.add(HttpHeaderNames.AUTHORITY, actualAuthority);
        }
    }
    private static void addHttp2Scheme(io.netty.handler.codec.http.HttpHeaders in, URI uri,
                                       RequestHeadersBuilder out) {
        final String value = uri.getScheme();
        if (value != null) {
            out.add(HttpHeaderNames.SCHEME, value);
            return;
        }
        final CharSequence cValue = in.get(ExtensionHeaderNames.SCHEME.text());
        if (cValue != null) {
            out.add(HttpHeaderNames.SCHEME, cValue.toString());
        } else {
            out.add(HttpHeaderNames.SCHEME, "unknown");
        }
    }
    public static Http2Headers toNettyHttp2(HttpHeaders in, boolean server) {
        final Http2Headers out = new DefaultHttp2Headers(false, in.size());
        if (server && !in.contains(HttpHeaderNames.STATUS)) {
            for (Entry<AsciiString, String> entry : in) {
                final AsciiString name = entry.getKey();
                final String value = entry.getValue();
                if (name.isEmpty() || isTrailerBlacklisted(name)) {
                    continue;
                }
                out.add(name, value);
            }
        } else {
            in.forEach((BiConsumer<AsciiString, String>) out::add);
            out.remove(HttpHeaderNames.CONNECTION);
            out.remove(HttpHeaderNames.TRANSFER_ENCODING);
        }
        if (!out.contains(HttpHeaderNames.COOKIE)) {
            return out;
        }
        final List<CharSequence> cookies = out.getAllAndRemove(HttpHeaderNames.COOKIE);
        for (CharSequence c : cookies) {
            out.add(HttpHeaderNames.COOKIE, COOKIE_SPLITTER.split(c));
        }
        return out;
    }
    public static void toNettyHttp1(
            int streamId, HttpHeaders inputHeaders, io.netty.handler.codec.http.HttpHeaders outputHeaders,
            HttpVersion httpVersion, boolean isTrailer, boolean isRequest) throws Http2Exception {
        final CharSequenceMap translations = isRequest ? REQUEST_HEADER_TRANSLATIONS
                                                       : RESPONSE_HEADER_TRANSLATIONS;
        StringJoiner cookieJoiner = null;
        try {
            for (Entry<AsciiString, String> entry : inputHeaders) {
                final AsciiString name = entry.getKey();
                final String value = entry.getValue();
                final AsciiString translatedName = translations.get(name);
                if (translatedName != null && !inputHeaders.contains(translatedName)) {
                    outputHeaders.add(translatedName, value);
                    continue;
                }
                if (name.isEmpty() || HTTP2_TO_HTTP_HEADER_BLACKLIST.contains(name)) {
                    continue;
                }
                if (isTrailer && isTrailerBlacklisted(name)) {
                    continue;
                }
                if (HttpHeaderNames.COOKIE.equals(name)) {
                    if (cookieJoiner == null) {
                        cookieJoiner = new StringJoiner(COOKIE_SEPARATOR);
                    }
                    COOKIE_SPLITTER.split(value).forEach(cookieJoiner::add);
                } else {
                    outputHeaders.add(name, value);
                }
            }
            if (cookieJoiner != null && cookieJoiner.length() != 0) {
                outputHeaders.add(HttpHeaderNames.COOKIE, cookieJoiner.toString());
            }
        } catch (Throwable t) {
            throw streamError(streamId, PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error");
        }
        if (!isTrailer) {
            HttpUtil.setKeepAlive(outputHeaders, httpVersion, true);
        }
    }
    public static ResponseHeaders setOrRemoveContentLength(ResponseHeaders headers, HttpData content,
                                                           HttpHeaders trailers) {
        requireNonNull(headers, "headers");
        requireNonNull(content, "content");
        requireNonNull(trailers, "trailers");
        final HttpStatus status = headers.status();
        if (isContentAlwaysEmptyWithValidation(status, content, trailers)) {
            if (status != HttpStatus.NOT_MODIFIED) {
                if (headers.contains(HttpHeaderNames.CONTENT_LENGTH)) {
                    final ResponseHeadersBuilder builder = headers.toBuilder();
                    builder.remove(HttpHeaderNames.CONTENT_LENGTH);
                    return builder.build();
                }
            } else {
            }
            return headers;
        }
        if (!trailers.isEmpty()) {
            if (headers.contains(HttpHeaderNames.CONTENT_LENGTH)) {
                final ResponseHeadersBuilder builder = headers.toBuilder();
                builder.remove(HttpHeaderNames.CONTENT_LENGTH);
                return builder.build();
            }
            return headers;
        }
        if (!headers.contains(HttpHeaderNames.CONTENT_LENGTH) || !content.isEmpty()) {
            return headers.toBuilder()
                          .setInt(HttpHeaderNames.CONTENT_LENGTH, content.length())
                          .build();
        }
        return headers;
    }
    private static String convertHeaderValue(AsciiString name, CharSequence value) {
        if (!(value instanceof AsciiString)) {
            return value.toString();
        }
        if (HEADER_VALUE_CACHE != null && CACHED_HEADERS.contains(name)) {
            final String converted = HEADER_VALUE_CACHE.get((AsciiString) value);
            assert converted != null; 
            return converted;
        }
        return value.toString();
    }
    public static boolean isTrailerBlacklisted(AsciiString name) {
        return HTTP_TRAILER_BLACKLIST.contains(name);
    }
    private static final class CharSequenceMap
            extends DefaultHeaders<AsciiString, AsciiString, CharSequenceMap> {
        CharSequenceMap() {
            super(HTTP2_HEADER_NAME_HASHER, UnsupportedValueConverter.instance());
        }
        @SuppressWarnings("unchecked")
        CharSequenceMap(int size) {
            super(HTTP2_HEADER_NAME_HASHER, UnsupportedValueConverter.instance(), NameValidator.NOT_NULL, size);
        }
    }
    private ArmeriaHttpUtil() {}
}
