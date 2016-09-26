package eu.marcocattaneo.instagramLibrary.connection.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

import eu.marcocattaneo.instagramLibrary.connection.client.implementation.HttpCallback;
import eu.marcocattaneo.instagramLibrary.connection.client.models.HttpResponse;

public class HttpClient {

    private RequestQueue mRequestQueue;

    private Context mContext;

    public HttpClient(Context context) {
        this.mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public Build Build(String url, HttpMethod method, String body) {

        return new Build(mContext, mRequestQueue, url, method, body);
    }

    public Build Build(String url, HttpMethod method) {

        return new Build(mContext, mRequestQueue, url, method, null);
    }

    public static class Build {

        private Context mContext;

        private String TAG = HttpClient.class.getSimpleName();

        private RequestQueue mRequestQueue;

        private String mUrl;

        private HttpMethod mMethod;

        private String mBody;

        protected Build(Context context, RequestQueue requestQueue, String url, HttpMethod method, String body) {
            this.mContext = context;
            this.mMethod = method;
            this.mRequestQueue = requestQueue;
            this.mUrl = url;
            this.mBody = body;
        }

        /**
         * Start http request
         */
        public void execute(final HttpCallback callback) {

            int intMethod = Request.Method.GET;
            switch (mMethod) {
                case GET:
                    intMethod = Request.Method.GET;
                    break;

                case POST:
                    intMethod = Request.Method.POST;
                    break;

                case DELETE:
                    intMethod = Request.Method.DELETE;
                    break;

                case PUT:
                    intMethod = Request.Method.PUT;
                    break;

                case HEAD:
                    intMethod = Request.Method.HEAD;
                    break;
            }

            final HttpRequest stringRequest = new HttpRequest(intMethod, mUrl, mBody, new Response.Listener<HttpResponse>() {
                @Override
                public void onResponse(HttpResponse response) {

                    String body = "";
                    try {
                        body = new String(response.getBody(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Response: " + ((response.getBody() != null) ? body : ""));
                    callback.onResponse(body, 200);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFail(500);
                }
            });

            stringRequest.setTag(TAG);

            this.mRequestQueue.add(stringRequest);
        }

        private void cancelAll() {
            mRequestQueue.cancelAll(TAG);
        }

    }

}
