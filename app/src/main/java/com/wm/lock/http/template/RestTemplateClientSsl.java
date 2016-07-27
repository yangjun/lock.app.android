package com.wm.lock.http.template;

import android.content.Context;

import com.wm.lock.LockConfig;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RestTemplateClientSsl {
    
    public static RestTemplate getRestTemplate(Context ctx, int port) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(getHttpClient(port)));
        restTemplate.setMessageConverters(getMessageConvertors());
        return restTemplate;
    }

    public static HttpClient getHttpClient(int port) {
        return HttpClientHelper.getHttpClient(port);
    }

    private static List<HttpMessageConverter<?>> getMessageConvertors() {
        List<HttpMessageConverter<?>> result = new ArrayList<>();
        result.add(new MappingJacksonHttpMessageConverter());
        result.add(new FormHttpMessageConverter());
        result.add(new ResourceHttpMessageConverter());
        result.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return result;
    }

//    public static HttpClient getNewHttpClient() {
//        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
//
//            HttpParams params = new BasicHttpParams();
//            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//            SchemeRegistry registry = new SchemeRegistry();
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//
//            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//            registry.register(new Scheme("https", sf, 443));
//
//            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//
//            return new DefaultHttpClient(ccm, params);
//        } catch (Exception e) {
//            return new DefaultHttpClient();
//        }
//    }
//
//    public static class MySSLSocketFactory extends SSLSocketFactory {
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//
//        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
//            super(truststore);
//
//            TrustManager tm = new X509TrustManager() {
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            };
//
//            sslContext.init(null, new TrustManager[] { tm }, null);
//        }
//
//        @Override
//        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
//            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//        }
//
//        @Override
//        public Socket createSocket() throws IOException {
//            return sslContext.getSocketFactory().createSocket();
//        }
//    }

//    private static HttpClient getSSLHttpClient(Context ctx, int port) throws Throwable{
//        BasicHttpParams params = new BasicHttpParams();
//        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//        HttpProtocolParams.setUseExpectContinue(params, true);
//
//        ConnManagerParams.setTimeout(params, CoreConfig.HTTP_READ_TIMEOUT);
//        HttpConnectionParams.setConnectionTimeout(params, CoreConfig.HTTP_CONN_TIMEOUT);
//        HttpConnectionParams.setSoTimeout(params, CoreConfig.HTTP_READ_TIMEOUT);
//
//        SchemeRegistry schReg = new SchemeRegistry();
//        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        SSLSocketFactory socketFactory = PartSocketFactory.getSFactory(ctx);
//        schReg.register(new Scheme("https", socketFactory, port));
//
//        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);
//        return new DefaultHttpClient(connMgr, params);
//    }
//
//    private static class PartSocketFactory extends SSLSocketFactory {
//
//        private static final String KEY_PASS = "123456";
//
//        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
//            public boolean verify(String arg0, SSLSession arg1) {
//                return true;
//            }
//            public void verify(String arg0, SSLSocket arg1) throws IOException {}
//            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
//            public void verify(String arg0, X509Certificate arg1) throws SSLException {}
//        };
//
//        public PartSocketFactory(KeyStore trustStore) throws Throwable {
//            super(trustStore);
//            setHostnameVerifier(hostnameVerifier);
//        }
//
//        public static SSLSocketFactory getSFactory(Context context) throws Throwable {
//            InputStream ins = context.getResources().openRawResource(R.raw.cert12306);
//            KeyStore trustStore = KeyStore.getInstance("bks");
//            try {
//                trustStore.load(ins, KEY_PASS.toCharArray());
//            }
//            finally {
//                ins.close();
//            }
//            SSLSocketFactory factory = new SSLSocketFactory(trustStore);
//            return factory;
//        }
//    }
//
//    private static class AllSSLSocketFactory extends SSLSocketFactory {
//
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//
//        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
//            public boolean verify(String arg0, SSLSession arg1) {
//                return true;
//            }
//            public void verify(String arg0, SSLSocket arg1) throws IOException {}
//            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
//            public void verify(String arg0, X509Certificate arg1) throws SSLException {}
//        };
//
//        public static SSLSocketFactory getSFactory(Context ctx) throws Throwable {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
//            SSLSocketFactory sf = new AllSSLSocketFactory(trustStore);
//            return sf;
//        }
//
//        public AllSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
//                KeyManagementException,
//                KeyStoreException,
//                UnrecoverableKeyException {
//            super(truststore);
//
//            TrustManager tm = new X509TrustManager() {
//
//                @Override
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//
//                @Override
//                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
//                                               String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
//                                               String authType) throws CertificateException {
//
//                }
//            };
//
//            sslContext.init(null, new TrustManager[]{tm}, null);
//            setHostnameVerifier(hostnameVerifier);
//        }
//
//        @Override
//        public Socket createSocket(Socket socket,
//                                   String host,
//                                   int port,
//                                   boolean autoClose) throws IOException,
//                UnknownHostException {
//            return sslContext.getSocketFactory().createSocket(socket,
//                                                              host,
//                                                              port,
//                                                              autoClose);
//        }
//
//        @Override
//        public Socket createSocket() throws IOException {
//            return sslContext.getSocketFactory().createSocket();
//        }
//    }


    static class HttpClientHelper {

        private static HttpClient httpClient;

        private HttpClientHelper() {
        }

        public static synchronized HttpClient getHttpClient(int port) {
            if (null == httpClient) {
                try {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    trustStore.load(null, null);
                    SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                    HttpParams params = new BasicHttpParams();

                    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
                    HttpProtocolParams.setUseExpectContinue(params, true);

                    ConnManagerParams.setTimeout(params, LockConfig.HTTP_READ_TIMEOUT);
                    HttpConnectionParams.setConnectionTimeout(params, LockConfig.HTTP_CONN_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(params, LockConfig.HTTP_READ_TIMEOUT);

                    SchemeRegistry schReg = new SchemeRegistry();
                    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                    schReg.register(new Scheme("https", sf, port));

                    ClientConnectionManager conManager = new ThreadSafeClientConnManager(params, schReg);

                    httpClient = new DefaultHttpClient(conManager, params);
                } catch (Exception e) {
                    return new DefaultHttpClient();
                }
            }
            return httpClient;
        }

    }

    static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}
