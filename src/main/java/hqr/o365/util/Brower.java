package hqr.o365.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * @author ruilong 
 * Brower simulator 
 * 1. by pass SSL
 * 2. Timeout 20s
 */
public class Brower {
	private static CloseableHttpClient httpclient = null;
	private static HttpClientContext httpClientContext = null;
	private static RequestConfig reqConfig = null;

	// bypass SSL cert
	public static CloseableHttpClient getCloseableHttpClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();

			SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

			httpclient = HttpClients.custom().setSSLSocketFactory(sslf).setMaxConnTotal(20).setMaxConnPerRoute(20).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return httpclient;
	}

	public static HttpClientContext getHttpClientContext() {
		httpClientContext = HttpClientContext.create();
		return httpClientContext;
	}

	public static RequestConfig getRequestConfig() {
		reqConfig = RequestConfig.custom().setConnectionRequestTimeout(20000).setConnectTimeout(20000).setSocketTimeout(20000).build();
		return reqConfig;
	}
}