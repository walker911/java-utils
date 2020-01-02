package com.walker.utils;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp util
 *
 * @author muqin
 * @date 2018/11/19
 */
public class OkHttpUtil {

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS)
            .writeTimeout(5L, TimeUnit.SECONDS)
            .build();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpUtil() {
    }

    /**
     * sync get
     * @param url
     * @return
     * @throws IOException
     */
    public static String syncGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    /**
     * async get
     * @param url
     */
    public static void asyncGet(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Objects.requireNonNull(response.body()).string();
            }
        });
    }

    /**
     * get with parameters
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String getWithParams(String url, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        params.forEach(urlBuilder::addQueryParameter);

        String urlStr = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    /**
     * post form
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach(builder::add);
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    /**
     * post json
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=utf-8")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    /**
     * upload file
     * URLEncoder.encode() 解决中文报异常
     *
     * @param key
     * @param url
     * @param file
     * @return
     */
    public static String uploadFile(String key, String url, File file) throws IOException {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(key, URLEncoder.encode(file.getName(), "UTF-8"),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        return URLDecoder.decode(Objects.requireNonNull(response.body()).string(), "UTF-8");
    }
}
