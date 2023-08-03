
package org.opencastproject.kernel.http.impl;
import org.opencastproject.kernel.http.api.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
public class HttpClientImpl implements HttpClient {
  private static final Logger logger = LoggerFactory.getLogger(HttpClientImpl.class);
  private DefaultHttpClient defaultHttpClient = makeHttpClient();
  @Override
  public HttpParams getParams() {
    return defaultHttpClient.getParams();
  }
  @Override
  public CredentialsProvider getCredentialsProvider() {
    return defaultHttpClient.getCredentialsProvider();
  }
  @Override
  public HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException {
    return defaultHttpClient.execute(httpUriRequest);
  }
  @Override
  public ClientConnectionManager getConnectionManager() {
    return defaultHttpClient.getConnectionManager();
  }
  private DefaultHttpClient makeHttpClient() {
    DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
    try {
      logger.debug("Installing forgiving hostname verifier and trust managers");
      X509TrustManager trustManager = createTrustManager();
      X509HostnameVerifier hostNameVerifier = createHostNameVerifier();
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
      SSLSocketFactory ssf = new SSLSocketFactory(sslContext, hostNameVerifier);
      ClientConnectionManager ccm = defaultHttpClient.getConnectionManager();
      SchemeRegistry sr = ccm.getSchemeRegistry();
      sr.register(new Scheme("https", 443, ssf));
    } catch (NoSuchAlgorithmException e) {
      logger.error("Error creating context to handle TLS connections: {}", e.getMessage());
    } catch (KeyManagementException e) {
      logger.error("Error creating context to handle TLS connections: {}", e.getMessage());
    }
    return defaultHttpClient;
  }
  private X509TrustManager createTrustManager() {
    X509TrustManager trustManager = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        logger.trace("Skipping trust check on client certificate {}", string);
      }
      public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        logger.trace("Skipping trust check on server certificate {}", string);
      }
      public X509Certificate[] getAcceptedIssuers() {
        logger.trace("Returning empty list of accepted issuers");
        return null;
      }
    };
    return trustManager;
  }
  private X509HostnameVerifier createHostNameVerifier() {
    X509HostnameVerifier verifier = new X509HostnameVerifier() {
      public void verify(String host, SSLSocket ssl) throws IOException {
        logger.trace("Skipping SSL host name check on {}", host);
      }
      public void verify(String host, X509Certificate xc) throws SSLException {
        logger.trace("Skipping X509 certificate host name check on {}", host);
      }
      public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        logger.trace("Skipping DNS host name check on {}", host);
      }
      public boolean verify(String host, SSLSession ssl) {
        logger.trace("Skipping SSL session host name check on {}", host);
        return true;
      }
    };
    return verifier;
  }
}
