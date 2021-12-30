package com.hinsliu.bestroute.utils.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author: Hins Liu
 * @description: Http Utils.
 */
@Slf4j
public class HttpUtil {

    public static final ContentType TEXT_PLAIN = ContentType.create("text/plain", StandardCharsets.UTF_8);

    /**
     * HttpClient 连接池
     */
    private static PoolingHttpClientConnectionManager cm = null;

    static {
        // 初始化连接池，可用于请求HTTP/HTTPS（信任所有证书）
        cm = new PoolingHttpClientConnectionManager(getRegistry());
        // 整个连接池最大连接数
        cm.setMaxTotal(200);
        // 每路由最大连接数，默认值是2
        cm.setDefaultMaxPerRoute(5);
    }

    /**
     * 发送 HTTP GET请求
     * <p>不带请求参数和请求头</p>
     * @param url 地址
     * @return
     * @throws Exception
     */
    public static String httpGet(String url) throws Exception {
        log.info("请求参数:{}",url);
        HttpGet httpGet = new HttpGet(url);

        return doHttp(httpGet);
    }

    /**
     * 发送 HTTP GET请求
     * <p>带请求参数，不带请求头</p>
     * @param url    地址
     * @param params 参数
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String httpGet(String url, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        // 装载请求地址和参数
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        ub.setParameters(pairs);
        HttpGet httpGet = new HttpGet(ub.build());

        return doHttp(httpGet);
    }

    /**
     * 发送 HTTP GET请求
     * <p>带请求参数和请求头</p>
     * @param url     地址
     * @param headers 请求头
     * @param params  参数
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String httpGet(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        // 装载请求地址和参数
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        // 设置请求头
        for (Map.Entry<String, Object> param : headers.entrySet()){
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));}

        return doHttp(httpGet);
    }

    /**
     * 发送 HTTP POST请求
     * <p>不带请求参数和请求头</p>
     *
     * @param url 地址
     * @return
     * @throws Exception
     */
    public static String httpPost(String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        return doHttp(httpPost);
    }

    /**
     * 发送 HTTP POST请求
     * <p>带请求参数，不带请求头</p>
     *
     * @param url    地址
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, Object> params) throws Exception {
        // 转换请求参数
        List<NameValuePair> pairs = covertParams2NVPS(params);

        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8.name()));

        return doHttp(httpPost);
    }

    /**
     * 发送 HTTP POST请求
     * <p>带请求参数和请求头</p>
     *
     * @param url     地址
     * @param headers 请求头
     * @param params  参数
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        log.info("POST请求参数:{}",params);
        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        StringEntity entity = new StringEntity(JSON.toJSONString(params),ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        // 设置请求头
        for (Map.Entry<String, Object> param : headers.entrySet()){
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));}

        return doHttp(httpPost);
    }




    /**
     * 转换请求参数
     *
     * @param params
     * @return
     */
    public static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();

        for (Map.Entry<String, Object> param : params.entrySet()){
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));}

        return pairs;
    }

    /**
     * 发送 HTTP 请求
     *
     * @param request
     * @return
     * @throws Exception
     */
    private static String doHttp(HttpRequestBase request) throws Exception {
        // 通过连接池获取连接对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        return doRequest(httpClient, request);
    }

    /**
     * 发送 HTTPS 请求
     * <p>使用指定的证书文件及密码</p>
     *
     * @param request
     * @param path
     * @param password
     * @return
     * @throws Exception
     * @throws Exception
     */
    private static String doHttps(HttpRequestBase request, String path, String password) throws Exception {
        // 获取HTTPS SSL证书
        SSLConnectionSocketFactory csf = getSSLFactory(path, password);
        // 通过连接池获取连接对象
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

        return doRequest(httpClient, request);
    }

    /**
     * 获取HTTPS SSL连接工厂
     * <p>使用指定的证书文件及密码</p>
     *
     * @param path     证书全路径
     * @param password 证书密码
     * @return
     * @throws Exception
     * @throws Exception
     */
    private static SSLConnectionSocketFactory getSSLFactory(String path, String password) throws Exception {

        // 初始化证书，指定证书类型为“PKCS12”
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 读取指定路径的证书
        FileInputStream input = new FileInputStream(new File(path));

        try {
            // 装载读取到的证书，并指定证书密码
            keyStore.load(input, password.toCharArray());
        } finally {
            input.close();
        }

        // 获取HTTPS SSL证书连接上下文
        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray()).build();

        // 获取HTTPS连接工厂，指定TSL版本
        SSLConnectionSocketFactory sslCsf = new SSLConnectionSocketFactory(sslContext, new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return sslCsf;
    }

    /**
     * 获取HTTPS SSL连接工厂
     * <p>跳过证书校验，即信任所有证书</p>
     *
     * @return
     * @throws Exception
     */
    private static SSLConnectionSocketFactory getSSLFactory() throws Exception {
        // 设置HTTPS SSL证书信息，跳过证书校验，即信任所有证书请求HTTPS
        SSLContextBuilder sslBuilder = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        });

        // 获取HTTPS SSL证书连接上下文
        SSLContext sslContext = sslBuilder.build();

        // 获取HTTPS连接工厂
        SSLConnectionSocketFactory sslCsf = new SSLConnectionSocketFactory(sslContext, new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

        return sslCsf;
    }

    /**
     * 获取 HTTPClient注册器
     *
     * @return
     * @throws Exception
     */
    private static Registry<ConnectionSocketFactory> getRegistry() {
        Registry<ConnectionSocketFactory> registry = null;

        try {
            registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", new PlainConnectionSocketFactory()).register("https", getSSLFactory()).build();
        } catch (Exception e) {
            log.error("获取 HTTPClient注册器失败", e);
        }

        return registry;
    }


    /**
     * 处理Http/Https请求，并返回请求结果
     * <p>注：默认请求编码方式 UTF-8</p>
     *
     * @param httpClient
     * @param request
     * @return
     * @throws Exception
     */
    private static String doRequest(CloseableHttpClient httpClient, HttpRequestBase request) throws Exception {
        String result = null;
        CloseableHttpResponse response = null;

        try {
            // 获取请求结果
            response = httpClient.execute(request);
            // 解析请求结果
            HttpEntity entity = response.getEntity();
            if ((entity.getContentEncoding() != null)
                    && entity.getContentEncoding().getValue().contains("gzip")) {
                GZIPInputStream gzip = new GZIPInputStream(
                        new ByteArrayInputStream(EntityUtils.toByteArray(entity)));
                InputStreamReader isr = new InputStreamReader(gzip);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String temp;
                while((temp = br.readLine()) != null){
                    sb.append(temp);
                    sb.append("\r\n");
                }
                isr.close();
                gzip.close();
                return sb.toString();
            }
            // 转换结果
            result = EntityUtils.toString(entity, StandardCharsets.UTF_8.name());
            // 关闭IO流
            EntityUtils.consume(entity);
        } finally {
            if (null != response){
                response.close();}
        }

        return result;
    }


}
