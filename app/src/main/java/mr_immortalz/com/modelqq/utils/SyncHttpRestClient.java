package mr_immortalz.com.modelqq.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * Created by DELL2 on 10/31/2016.
 */
public class SyncHttpRestClient {

   // private static final String BASE_URL ="https://urbanx-urbanxdevelopment.azurewebsites.net/api/v1.0/";
    private static SyncHttpClient client = new SyncHttpClient();

    public static void get(String url,boolean requireAuthentication , String AccessToken, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(20000);
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        client.addHeader("cache-control", "no-cache");
        client.addHeader("Accept", "application/json");
        client.setSSLSocketFactory(SSLSocketFactory.getSocketFactory());
        if (!AccessToken.isEmpty()) {
            client.addHeader("Authorization", "bearer " + AccessToken);
        }
        String URL = getAbsoluteUrl(url);
        client.get(URL, params, responseHandler);
    }

    public static void post(String url,boolean requireAuthentication , String AccessToken, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(20000);
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        client.addHeader("cache-control", "no-cache");
        client.addHeader("Accept", "application/json");
        client.setSSLSocketFactory(SSLSocketFactory.getSocketFactory());
        if (!AccessToken.isEmpty()) {
            client.addHeader("Authorization", "bearer " + AccessToken);
        }
        String URL = getAbsoluteUrl(url);
        client.post(URL, params, responseHandler);
    }

    public static void post(String url,boolean requireAuthentication ,String AccessToken, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(20000);
        client.addHeader("content-type", "application/x-www-form-urlencoded");
        client.addHeader("cache-control", "no-cache");
        client.addHeader("Accept", "application/json");
        client.setSSLSocketFactory(SSLSocketFactory.getSocketFactory());
        if (!AccessToken.isEmpty()) {
            client.addHeader("Authorization", "bearer " + AccessToken);
        }
        String URL = getAbsoluteUrl(url);
        client.post(URL, params, responseHandler);

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return  relativeUrl;
    }
}
