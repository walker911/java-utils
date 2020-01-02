package com.walker.utils.cookie;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MailUtil {
    public static void main(String[] args) throws IOException {
        RateLimiter rateLimiter = RateLimiter.create(1);
        for (int i = 0; i < 10; i++) {
            if (!rateLimiter.tryAcquire()) {
                System.out.println("wait...");
            }
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
    }

    public static String suggestion() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://apis.map.qq.com/ws/place/v1/" +
                "suggestion?region=上海市&keyword=灵石路&key=QC5BZ-ZYFEU-WZVVT-2SVNW-FV5LT-GMFXE");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String json = EntityUtils.toString(response.getEntity());
        return json;
    }
}
