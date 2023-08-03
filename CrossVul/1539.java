
package com.linecorp.armeria.common;
import static java.util.Objects.requireNonNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableMap;
import io.netty.util.AsciiString;
public final class HttpHeaderNames {
    public static final AsciiString METHOD = create(":method");
    public static final AsciiString SCHEME = create(":scheme");
    public static final AsciiString AUTHORITY = create(":authority");
    public static final AsciiString PATH = create(":path");
    public static final AsciiString STATUS = create(":status");
    public static final AsciiString CACHE_CONTROL = create("Cache-Control");
    public static final AsciiString CONTENT_LENGTH = create("Content-Length");
    public static final AsciiString CONTENT_TYPE = create("Content-Type");
    public static final AsciiString DATE = create("Date");
    public static final AsciiString PRAGMA = create("Pragma");
    public static final AsciiString VIA = create("Via");
    public static final AsciiString WARNING = create("Warning");
    public static final AsciiString ACCEPT = create("Accept");
    public static final AsciiString ACCEPT_CHARSET = create("Accept-Charset");
    public static final AsciiString ACCEPT_ENCODING = create("Accept-Encoding");
    public static final AsciiString ACCEPT_LANGUAGE = create("Accept-Language");
    public static final AsciiString ACCESS_CONTROL_REQUEST_HEADERS = create("Access-Control-Request-Headers");
    public static final AsciiString ACCESS_CONTROL_REQUEST_METHOD = create("Access-Control-Request-Method");
    public static final AsciiString AUTHORIZATION = create("Authorization");
    public static final AsciiString CONNECTION = create("Connection");
    public static final AsciiString COOKIE = create("Cookie");
    public static final AsciiString EARLY_DATA = create("Early-Data");
    public static final AsciiString EXPECT = create("Expect");
    public static final AsciiString FROM = create("From");
    public static final AsciiString FORWARDED = create("Forwarded");
    public static final AsciiString FOLLOW_ONLY_WHEN_PRERENDER_SHOWN =
            create("Follow-Only-When-Prerender-Shown");
    public static final AsciiString HOST = create("Host");
    public static final AsciiString HTTP2_SETTINGS = create("HTTP2-Settings");
    public static final AsciiString IF_MATCH = create("If-Match");
    public static final AsciiString IF_MODIFIED_SINCE = create("If-Modified-Since");
    public static final AsciiString IF_NONE_MATCH = create("If-None-Match");
    public static final AsciiString IF_RANGE = create("If-Range");
    public static final AsciiString IF_UNMODIFIED_SINCE = create("If-Unmodified-Since");
    public static final AsciiString LAST_EVENT_ID = create("Last-Event-ID");
    public static final AsciiString MAX_FORWARDS = create("Max-Forwards");
    public static final AsciiString ORIGIN = create("Origin");
    public static final AsciiString PREFER = create("Prefer");
    public static final AsciiString PROXY_AUTHORIZATION = create("Proxy-Authorization");
    public static final AsciiString RANGE = create("Range");
    public static final AsciiString REFERER = create("Referer");
    public static final AsciiString REFERRER_POLICY = create("Referrer-Policy");
    public static final AsciiString SERVICE_WORKER = create("Service-Worker");
    public static final AsciiString TE = create("TE");
    public static final AsciiString UPGRADE = create("Upgrade");
    public static final AsciiString USER_AGENT = create("User-Agent");
    public static final AsciiString ACCEPT_RANGES = create("Accept-Ranges");
    public static final AsciiString ACCEPT_PATCH = create("Accept-Patch");
    public static final AsciiString ACCESS_CONTROL_ALLOW_HEADERS = create("Access-Control-Allow-Headers");
    public static final AsciiString ACCESS_CONTROL_ALLOW_METHODS = create("Access-Control-Allow-Methods");
    public static final AsciiString ACCESS_CONTROL_ALLOW_ORIGIN = create("Access-Control-Allow-Origin");
    public static final AsciiString ACCESS_CONTROL_ALLOW_CREDENTIALS =
            create("Access-Control-Allow-Credentials");
    public static final AsciiString ACCESS_CONTROL_EXPOSE_HEADERS = create("Access-Control-Expose-Headers");
    public static final AsciiString ACCESS_CONTROL_MAX_AGE = create("Access-Control-Max-Age");
    public static final AsciiString AGE = create("Age");
    public static final AsciiString ALLOW = create("Allow");
    public static final AsciiString CONTENT_BASE = create("Content-Base");
    public static final AsciiString CONTENT_DISPOSITION = create("Content-Disposition");
    public static final AsciiString CONTENT_ENCODING = create("Content-Encoding");
    public static final AsciiString CONTENT_LANGUAGE = create("Content-Language");
    public static final AsciiString CONTENT_LOCATION = create("Content-Location");
    public static final AsciiString CONTENT_MD5 = create("Content-MD5");
    public static final AsciiString CONTENT_RANGE = create("Content-Range");
    public static final AsciiString CONTENT_SECURITY_POLICY = create("Content-Security-Policy");
    public static final AsciiString CONTENT_SECURITY_POLICY_REPORT_ONLY =
            create("Content-Security-Policy-Report-Only");
    public static final AsciiString ETAG = create("ETag");
    public static final AsciiString EXPIRES = create("Expires");
    public static final AsciiString LAST_MODIFIED = create("Last-Modified");
    public static final AsciiString LINK = create("Link");
    public static final AsciiString LOCATION = create("Location");
    public static final AsciiString ORIGIN_TRIAL = create("Origin-Trial");
    public static final AsciiString P3P = create("P3P");
    public static final AsciiString PROXY_AUTHENTICATE = create("Proxy-Authenticate");
    public static final AsciiString REFRESH = create("Refresh");
    public static final AsciiString REPORT_TO = create("Report-To");
    public static final AsciiString RETRY_AFTER = create("Retry-After");
    public static final AsciiString SERVER = create("Server");
    public static final AsciiString SERVER_TIMING = create("Server-Timing");
    public static final AsciiString SERVICE_WORKER_ALLOWED = create("Service-Worker-Allowed");
    public static final AsciiString SET_COOKIE = create("Set-Cookie");
    public static final AsciiString SET_COOKIE2 = create("Set-Cookie2");
    public static final AsciiString SOURCE_MAP = create("SourceMap");
    public static final AsciiString STRICT_TRANSPORT_SECURITY = create("Strict-Transport-Security");
    public static final AsciiString TIMING_ALLOW_ORIGIN = create("Timing-Allow-Origin");
    public static final AsciiString TRAILER = create("Trailer");
    public static final AsciiString TRANSFER_ENCODING = create("Transfer-Encoding");
    public static final AsciiString VARY = create("Vary");
    public static final AsciiString WWW_AUTHENTICATE = create("WWW-Authenticate");
    public static final AsciiString DNT = create("DNT");
    public static final AsciiString X_CONTENT_TYPE_OPTIONS = create("X-Content-Type-Options");
    public static final AsciiString X_DO_NOT_TRACK = create("X-Do-Not-Track");
    public static final AsciiString X_FORWARDED_FOR = create("X-Forwarded-For");
    public static final AsciiString X_FORWARDED_PROTO = create("X-Forwarded-Proto");
    public static final AsciiString X_FORWARDED_HOST = create("X-Forwarded-Host");
    public static final AsciiString X_FORWARDED_PORT = create("X-Forwarded-Port");
    public static final AsciiString X_FRAME_OPTIONS = create("X-Frame-Options");
    public static final AsciiString X_POWERED_BY = create("X-Powered-By");
    public static final AsciiString PUBLIC_KEY_PINS = create("Public-Key-Pins");
    public static final AsciiString PUBLIC_KEY_PINS_REPORT_ONLY = create("Public-Key-Pins-Report-Only");
    public static final AsciiString X_REQUESTED_WITH = create("X-Requested-With");
    public static final AsciiString X_USER_IP = create("X-User-IP");
    public static final AsciiString X_DOWNLOAD_OPTIONS = create("X-Download-Options");
    public static final AsciiString X_XSS_PROTECTION = create("X-XSS-Protection");
    public static final AsciiString X_DNS_PREFETCH_CONTROL = create("X-DNS-Prefetch-Control");
    public static final AsciiString PING_FROM = create("Ping-From");
    public static final AsciiString PING_TO = create("Ping-To");
    public static final AsciiString SEC_TOKEN_BINDING = create("Sec-Token-Binding");
    public static final AsciiString SEC_PROVIDED_TOKEN_BINDING_ID = create("Sec-Provided-Token-Binding-ID");
    public static final AsciiString SEC_REFERRED_TOKEN_BINDING_ID = create("Sec-Referred-Token-Binding-ID");
    private static final Map<CharSequence, AsciiString> map;
    static {
        final ImmutableMap.Builder<CharSequence, AsciiString> builder = ImmutableMap.builder();
        for (Field f : HttpHeaderNames.class.getDeclaredFields()) {
            final int m = f.getModifiers();
            if (Modifier.isPublic(m) && Modifier.isStatic(m) && Modifier.isFinal(m) &&
                f.getType() == AsciiString.class) {
                final AsciiString name;
                try {
                    name = (AsciiString) f.get(null);
                } catch (Exception e) {
                    throw new Error(e);
                }
                builder.put(name, name);
                builder.put(name.toString(), name);
            }
        }
        map = builder.build();
    }
    public static AsciiString of(CharSequence name) {
        if (name instanceof AsciiString) {
            return of((AsciiString) name);
        }
        final String lowerCased = Ascii.toLowerCase(requireNonNull(name, "name"));
        final AsciiString cached = map.get(lowerCased);
        return cached != null ? cached : AsciiString.cached(lowerCased);
    }
    public static AsciiString of(AsciiString name) {
        final AsciiString lowerCased = name.toLowerCase();
        final AsciiString cached = map.get(lowerCased);
        return cached != null ? cached : lowerCased;
    }
    private static AsciiString create(String name) {
        return AsciiString.cached(Ascii.toLowerCase(name));
    }
    private HttpHeaderNames() {}
}
