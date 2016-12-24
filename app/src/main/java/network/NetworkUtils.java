package network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utilities.AndroidUtilities;

import static network.APIConstants.HTTP;
import static network.APIConstants.HTTPS;
import static network.APIConstants.HTTP_AGENT;
import static network.APIConstants.USER_AGENT;

/**
 * Created by samarth on 12/23/16.
 */

public class NetworkUtils {

    public static final String TAG = "NetworkUtils";
    public static final int CONNECTION_TIMEOUT_VALUE = 300;
    public static final int READ_TIMEOUT_VALUE = 300;
    private static final String CONTENT_TYPE_TAG = "Content-Type";
    private static final String HTTP_HEADER_JSON = "application/json";
    private static final String HTTP_HEADER_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String ENCODING = "charset=utf-8";
    private static final long FILE_CONNECTION_TIMEOUT_VALUE = 5;
    private static final long FILE_READ_TIMEOUT_VALUE = 5;

    public static final int REQUEST_TYPE_POST = 1;

    public static final int REQUEST_TYPE_GET = 0;

    private static final MediaType JSON = MediaType.parse(HTTP_HEADER_JSON + "; " +
            ENCODING);
    private static final MediaType URLENCODED = MediaType.parse(HTTP_HEADER_FORM_URLENCODED + "; " +
            ENCODING);


    private static volatile OkHttpClient okHttpClient;

    public static OkHttpClient getClient() {
        OkHttpClient localInstance = okHttpClient;
        if (null == localInstance) {
            synchronized (NetworkUtils.class) {
                localInstance = okHttpClient;
                if (null == localInstance) {
                    okHttpClient = localInstance = new OkHttpClient();
                }
            }
        }
        return localInstance;
    }

    protected static String encodeAndFormatUrl(String url) {
        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            url = url.replaceAll("#", "%23");
            url = url.replace(" ", "%20");
        }
        return url;
    }

    private static NetworkException getCustomException(String message) {
        NetworkException.NetworkExceptionBuilder nBuilder
                = new NetworkException.NetworkExceptionBuilder();
        nBuilder.errorMessage(message);
        return nBuilder.createNetworkException();
    }


    @NonNull
    private static Call asyncExecuteRequest(OkHttpClient client, final Request request
            , final int requestCode, final NetworkListenerCallback callback) {
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback == null) {
                    return;
                }
                final NetworkException.NetworkExceptionBuilder nBuilder
                        = new NetworkException.NetworkExceptionBuilder();
                nBuilder.errorMessage("IOException while executing request - " + e.getMessage());
                nBuilder.cause(e);

                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onNetworkRequestFailure(requestCode, nBuilder.createNetworkException());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (callback == null) {
                    return;
                }
                if (response.isSuccessful()) {
                    final String responseString = response.body().string();

//                    if (!TextUtils.isEmpty(response.header(RoposoPreferenceManager.USER_ID_USR)))
//                        new RoposoPreferenceManager().saveStringData(
//                                response.header(RoposoPreferenceManager.USER_ID_USR)
//                                , RoposoPreferenceManager.USER_ID_USR);

                    //TODO above commented
                 /*   if (!TextUtils.isEmpty(response.header(RoposoPreferenceManager.USER_ID_USR)))
                        new RoposoPreferenceManager().saveStringData(
                                response.header(RoposoPreferenceManager.USER_ID_USR)
                                , RoposoPreferenceManager.USER_ID_USR);*/
                    //updateCookie(response.headers("Set-Cookie"));
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onNetworkRequestSuccess(requestCode, responseString);
                        }
                    });
                    response.body().close();
                } else {
                    String messageFromServer = response.message();
                    final NetworkException.NetworkExceptionBuilder nBuilder
                            = new NetworkException.NetworkExceptionBuilder();
                    nBuilder.errorMessage(response.body().string());
                    nBuilder.httpStatusCode(response.code());
                    nBuilder.messageFromServer(messageFromServer);
                    response.body().close();
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onNetworkRequestFailure(requestCode, nBuilder.createNetworkException());
                        }
                    });
                }
            }
        });
        return call;
    }


    public static Call asyncGET(final int requestCode, String url, Map<String, String> queryParams,
                                Map<String, String> headerParams,
                                final NetworkListenerCallback callback) {
        try {

            if (TextUtils.isEmpty(url)) {
                callback.onNetworkRequestFailure(requestCode, getCustomException("Empty URL!"));
                return null;
            }

//            url = encodeAndFormatUrl(url);
//            url = getUrlWithParams(url, queryParams);

            Uri.Builder builder = Uri.parse(url).buildUpon();
            url = builder.build().toString();

            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.tag(requestCode).url(url);

            addGETRequestHeaders(requestBuilder);

            if (headerParams != null) {
                for (String key : headerParams.keySet()) {
                    requestBuilder.addHeader(key, headerParams.get(key));
                }
            }

            Request request = requestBuilder.build();
            return asyncExecuteRequest(getOkHttpClient(), request, requestCode, callback);
        } catch (Exception e) {
            return null;
        }
    }

    private static void addGETRequestHeaders(Request.Builder builder) {
        builder.addHeader("Accept", "application/json");
        builder.addHeader("Content-Encoding", "gzip");
        builder.addHeader("Content-type", "application/json");
        builder.addHeader("Cache-Control", "public, no-cache, no-store");
        //builder.addHeader("Accept-Encoding", "gzip");
        builder.addHeader(USER_AGENT, System.getProperty(HTTP_AGENT));
    }

    private static OkHttpClient getOkHttpClient() {
        return getClient().newBuilder()
                .readTimeout(READ_TIMEOUT_VALUE, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT_VALUE, TimeUnit.SECONDS)
                .build();
    }

    public static class NetworkException extends Exception {
        public static final int HTTP_STATUS_NOT_AVAILABLE = -1;
        private String errorMessage;
        private int httpStatusCode;
        private String messageFromServer;

        private NetworkException(Throwable cause, int httpStatusCode, String errorMessage,
                                 String messageFromServer) {
            super(errorMessage, cause);
            this.errorMessage = errorMessage;
            this.httpStatusCode = httpStatusCode;
            this.messageFromServer = messageFromServer;
        }

        @Override
        public String getMessage() {
            return errorMessage;
        }

        public String getMessageFromServer() {
            return messageFromServer;
        }

        public int getHttpStatusCode() {
            return httpStatusCode;
        }

        public static class NetworkExceptionBuilder {

            private String errorMessage;
            private int httpStatusCode = HTTP_STATUS_NOT_AVAILABLE;
            private String messageFromServer;
            private Throwable cause;

            public NetworkExceptionBuilder() {
            }

            public NetworkExceptionBuilder errorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
                return this;
            }

            public NetworkExceptionBuilder httpStatusCode(int httpStatusCode) {
                this.httpStatusCode = httpStatusCode;
                return this;
            }

            public NetworkExceptionBuilder messageFromServer(String messageFromServer) {
                this.messageFromServer = messageFromServer;
                return this;
            }

            public NetworkExceptionBuilder cause(Throwable th) {
                this.cause = th;
                return this;
            }

            public NetworkException createNetworkException() {
                return new NetworkException(cause, httpStatusCode, errorMessage, messageFromServer);
            }
        }
    }
}
