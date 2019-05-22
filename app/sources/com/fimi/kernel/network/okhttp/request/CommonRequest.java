package com.fimi.kernel.network.okhttp.request;

import ch.qos.logback.classic.spi.CallerData;
import com.baidu.tts.loopj.AsyncHttpClient;
import com.baidu.tts.loopj.RequestParams;
import java.io.File;
import java.util.Map.Entry;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CommonRequest {
    private static final MediaType FILE_TYPE = MediaType.parse(RequestParams.APPLICATION_OCTET_STREAM);

    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
        Builder mFormBodyBuild = new Builder();
        if (params != null) {
            for (Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Entry<String, String> entry2 : headers.urlParams.entrySet()) {
                mHeaderBuild.add((String) entry2.getKey(), (String) entry2.getValue());
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        return new Request.Builder().url(url).post(mFormBody).headers(mHeaderBuild.build()).build();
    }

    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    public static Request createGetRequest(String url) {
        return createGetRequest(url, null);
    }

    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append(CallerData.NA);
        if (params != null) {
            for (Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append((String) entry.getKey()).append("=").append((String) entry.getValue()).append("&");
            }
        }
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Entry<String, String> entry2 : headers.urlParams.entrySet()) {
                mHeaderBuild.add((String) entry2.getKey(), (String) entry2.getValue());
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().headers(mHeaderBuild.build()).build();
    }

    public static Request createMonitorRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("&");
        if (params != null && params.hasParams()) {
            for (Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append((String) entry.getKey()).append("=").append((String) entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
    }

    public static Request createMultiPostRequest(String url, RequestParams params) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {
            for (Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(Headers.of(AsyncHttpClient.HEADER_CONTENT_DISPOSITION, "form-data; name=\"" + ((String) entry.getKey()) + "\""), RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {
                    requestBody.addPart(Headers.of(AsyncHttpClient.HEADER_CONTENT_DISPOSITION, "form-data; name=\"" + ((String) entry.getKey()) + "\""), RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }

    public static Request createGetOriginalRequest(String url, RequestParams params) {
        return createGetOriginalRequest(url, params, null);
    }

    public static Request createGetOriginalRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append(CallerData.NA);
        if (params != null) {
            for (Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append((String) entry.getKey()).append("=").append((String) entry.getValue()).append("&");
            }
        }
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Entry<String, String> entry2 : headers.urlParams.entrySet()) {
                mHeaderBuild.add((String) entry2.getKey(), (String) entry2.getValue());
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().headers(mHeaderBuild.build()).build();
    }

    public static Request createPostOriginalRequest(String url, RequestParams params) {
        return createPostOriginalRequest(url, params, null);
    }

    public static Request createPostOriginalRequest(String url, RequestParams params, RequestParams headers) {
        Builder mFormBodyBuild = new Builder();
        if (params != null) {
            for (Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Entry<String, String> entry2 : headers.urlParams.entrySet()) {
                mHeaderBuild.add((String) entry2.getKey(), (String) entry2.getValue());
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        return new Request.Builder().url(url).post(mFormBody).headers(mHeaderBuild.build()).build();
    }
}
