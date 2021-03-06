package com.thinkgem.jeesite.util;


import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("deprecation")
public class WebRequestUtil {
    private static final int MAX_CONNECTION_SIZE = 200;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 30000;
    private static CloseableHttpClient hc = null;
    private static RequestConfig rc = null;

    static {
        InputStream input = WebRequestUtil.class.getResourceAsStream("_dev_debug_config.properties");
        if (input != null) {
            Properties prop = new Properties();
            try {
                prop.load(input);
                DebugConfig.init(prop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调试配置
     */
    public static class DebugConfig {
        private static DebugConfig instance = new DebugConfig();

        private DebugConfig() {
        }

        public static DebugConfig getInstance() {
            return instance;
        }

        public static final void init(Properties properties) {
            synchronized (DebugConfig.class) {
                if (instance == null) {
                    instance = new DebugConfig();
                }
                if (properties != null) {
                    instance.setDebugDubboVersion(properties.getProperty("debug.dubbo.version"));
                }
            }
        }

        private String debugDubboVersion;

        public String getDebugDubboVersion() {
            return debugDubboVersion;
        }

        void setDebugDubboVersion(String debugDubboVersion) {
            if (debugDubboVersion != null && debugDubboVersion.length() != 0) {
                this.debugDubboVersion = debugDubboVersion;
                System.out.println("debug.dubbo.version:" + debugDubboVersion);
            }
        }
    }


    private static void setDebugDubboVersion(HttpRequestBase requestBase) {
        String debugVersion = DebugConfig.getInstance().getDebugDubboVersion();
        if (debugVersion != null && debugVersion.length() != 0) {
            requestBase.setHeader("DUBBO-VERSION", "YJJ");//debugVersion);
        }
    }

    synchronized private static CloseableHttpClient getHttpClient() {
        if (hc == null) {
            if (hc == null) {
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                cm.setMaxTotal(MAX_CONNECTION_SIZE);
                cm.setDefaultMaxPerRoute(20);
                cm.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Consts.UTF_8).build());
                hc = HttpClients.custom().setConnectionManager(cm).setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

                    public long getKeepAliveDuration(HttpResponse arg0, org.apache.http.protocol.HttpContext arg1) {
                        return 30000;
                    }
                }).build();
                rc = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(
                        CONNECTION_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).setExpectContinueEnabled(
                        false).setRedirectsEnabled(false).build();
            }
        }
        return hc;
    }

    public static String getResponseString(String baseUrl, String params, boolean useGzip) {
        CloseableHttpResponse resp = null;
        try {
            resp = getHttpResponse(baseUrl, params, "", useGzip);
            String result = EntityUtils.toString(resp.getEntity(), "utf-8");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(baseUrl + " " + params, e);
        } finally {
            if (resp != null) {
                try {
                    resp.close();
                } catch (Exception e) {
                    throw new RuntimeException(baseUrl + " " + params, e);
                }
            }
        }
    }



    private static CloseableHttpResponse getHttpResponse(String baseUrl, String params, String cid,
                                                         boolean useGzip) throws ClientProtocolException, IOException {
        CloseableHttpClient client = getHttpClient();
        HttpRequestBase req = null;
        if (params == null) {
            req = new HttpGet(baseUrl);
        } else if (params.length() > 20) {
            System.out.println("Post请求:" + params);
            HttpPost post = new HttpPost(baseUrl);
            StringEntity se = new StringEntity(params, "utf-8");
            se.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
            post.setEntity(se);
            req = post;
        } else {
            System.out.println("Get请求:" + baseUrl + "?" + params);
            req = new HttpGet(baseUrl + "?" + params);
        }
        setDebugDubboVersion(req);
        req.setConfig(rc);
        if (useGzip) {
            req.setHeader("Accept-Encoding", "gzip");
        }
        CloseableHttpResponse resp = null;
        resp = client.execute(req);
        int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException(cid + ",0 " + baseUrl + "?" + params + " code:" + statusCode);
        }

        return resp;
    }




}
