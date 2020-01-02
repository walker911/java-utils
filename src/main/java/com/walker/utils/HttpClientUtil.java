package com.walker.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Connection Timeout: the time to establish the connection with the remote host.
 * Socket Timeout: the time waiting for data - after the connection was established;
 * maximum time of inactivity between two data packets.
 * Connection Manager Timeout: the time to wait for a connection from the connection manager/pool.
 *
 * @author walker
 * @date 2018/11/19
 */
public class HttpClientUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final int CONNECT_TIMEOUT = 5000;

    private static final int SOCKET_TIMEOUT = 5000;

    private static final int CONNECTION_REQUEST_TIMEOUT = 5000;

    private static RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .build();

    private HttpClientUtil() {
    }

    /**
     * get
     *
     * @param url 地址
     * @return result
     * @throws IOException IOException
     */
    public static String doGet(String url) throws IOException {
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpGet getMethod = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(getMethod)) {
                return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
        }
    }

    /**
     * form post
     *
     * @param url    地址
     * @param params 参数
     * @return result
     * @throws IOException IOException
     */
    public static String formPost(String url, Map<String, String> params) throws IOException {
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> pairs = new ArrayList<>();
            params.forEach((key, value) -> pairs.add(new BasicNameValuePair(key, value)));
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
        }
    }

    /**
     * json post
     *
     * @param url  地址
     * @param json json字符串
     * @return result
     * @throws IOException IOException
     */
    public static String jsonPost(String url, String json) throws IOException {
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpPost httpPost = new HttpPost(url);

            // ISO-8859-1 修改为 UTF-8
            StringEntity entity = new StringEntity(json, DEFAULT_CHARSET);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
            httpPost.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
        }
    }

    /**
     * authorization post
     *
     * @param url 地址
     * @return result
     * @throws IOException             IOException
     * @throws AuthenticationException AuthenticationException
     */
    public static String basicAuthorizationPost(String url) throws IOException, AuthenticationException {
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity("test post"));
            UsernamePasswordCredentials credentials =
                    new UsernamePasswordCredentials("username", "password");
            httpPost.addHeader(new BasicScheme().authenticate(credentials, httpPost, null));

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    /**
     * 上传文件
     *
     * @param key file or files
     * @param url 地址
     * @param files 文件数组
     * @return result
     * @throws IOException IOException
     */
    public static String uploadFile(String key, String url, List<File> files) throws IOException {
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    // 解决中文文件名乱码
                    .setMode(HttpMultipartMode.RFC6532);
            files.forEach(file -> builder.addPart(key, new FileBody(file)));

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
        }
    }

    /**
     * 上传文件
     *
     * @param key file or files
     * @param url 地址
     * @param stream InputStream
     * @return result
     * @throws IOException IOException
     */
    public static String uploadFile(String key, String url, String filename, InputStream stream) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.RFC6532);
            builder.addBinaryBody(key, stream, ContentType.MULTIPART_FORM_DATA, filename);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
        }
    }

    /**
     *
     * @param url url with ssl
     * @return result
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static String doGetWithSSL(String url) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true).build();

        try (CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/xml");

            try (CloseableHttpResponse response = client.execute(httpGet)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8091/api/za/renewal";
        Map<String, String> params = new HashMap<>();
        params.put("licenseNo", "闽A360B9");
        params.put("carKindCode", "02");
        params.put("engine", "MK45074J11");
        params.put("vin", "JE3AZ5929EZ000569");
        params.put("vehicleKindCode", "A01");
        params.put("brandName", "欧蓝德OUTLANDER 2.0L越野车");
        params.put("enrollDate", "2015-09-19");
        params.put("carUsedType", "211");
        params.put("vehicleSeat", "5");
        params.put("token", "88e670dc5d7b4ddf83a6a8fc05c2aef1");
        params.put("source", "6");
        params.put("city", "350100");
        params.put("timestamp", "1565058523401");
        params.put("sign", "871f67546e32fe2104575527da5f5a6b");
        String result = formPost(url, params);
        System.out.println(result);
    }
}
