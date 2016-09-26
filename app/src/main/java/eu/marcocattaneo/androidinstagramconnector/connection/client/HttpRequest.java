package eu.marcocattaneo.androidinstagramconnector.connection.client;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

import eu.marcocattaneo.androidinstagramconnector.connection.client.models.HttpResponse;

public class HttpRequest extends Request<HttpResponse> {

    public static final String TAG = HttpRequest.class.getSimpleName();

    private Map<String, String> mHeaderMaps = new HashMap<>();

    private Response.Listener<HttpResponse> mListener;

    private String mBody;

    public HttpRequest(int method, String url, String body, Response.Listener<HttpResponse> listener, Response.ErrorListener errorListener) {
        super(method, url,errorListener);

        this.mListener = listener;
        this.mBody = body;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaderMaps;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return mBody != null ? mBody.getBytes() : super.getBody();
    }

    @Override
    protected Response<HttpResponse> parseNetworkResponse(NetworkResponse response) {
        HttpResponse parsed;
        parsed = new HttpResponse(response.statusCode, response.data);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(HttpResponse response) {

        mListener.onResponse(response);
    }

    /**
     * Add header value
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        mHeaderMaps.put(key, value);
    }

    /**
     * Remove header value
     * @param key
     */
    public void removeHeader(String key) {
        if (mHeaderMaps.containsKey(key))
            mHeaderMaps.remove(key);
    }

}

