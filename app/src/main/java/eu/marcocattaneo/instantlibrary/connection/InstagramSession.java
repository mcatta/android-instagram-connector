package eu.marcocattaneo.instantlibrary.connection;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import eu.marcocattaneo.instantlibrary.connection.client.HttpClient;
import eu.marcocattaneo.instantlibrary.connection.client.HttpMethod;
import eu.marcocattaneo.instantlibrary.connection.client.implementation.HttpCallback;
import eu.marcocattaneo.instantlibrary.connection.implementation.InstagramListener;
import eu.marcocattaneo.instantlibrary.connection.implementation.RequestCallback;
import eu.marcocattaneo.instantlibrary.connection.models.ConnectionError;
import eu.marcocattaneo.instantlibrary.connection.utils.AuthenticationDialog;
import eu.marcocattaneo.instantlibrary.connection.utils.HttpUtils;

public class InstagramSession {

    public enum STATUS {
        NOT_READY, AUTHORIZATION, CONNECTED, ERROR, REQUIRING_TOKEN
    }

    private static final String PREF_SHARED_TOKEN = "InstantLibrary:saved_token";

    private static final String ENDPOINT_URL = "https://api.instagram.com";

    private static final String AUTHORIZATION_URL = ENDPOINT_URL + "/oauth/authorize/";

    private static final String OAUTH_URL = ENDPOINT_URL + "/oauth/access_token";

    private SharedPreferences mSharedPreferences;

    private String clientId;

    private String clientSecret;

    private String clientCallback;

    private STATUS mCurrentStatus = STATUS.NOT_READY;

    private InstagramListener mListener;

    private Activity mActivity;

    private HttpClient mHttpClient;

    public InstagramSession(@NonNull Activity activity) {
        this.mActivity = activity;
        initPreferences();
    }

    public void loadSession(Instagram instagram, InstagramListener instagramListener) {

        this.mListener = instagramListener;
        this.mHttpClient = new HttpClient(mActivity);

        this.clientCallback = instagram.getClientCallback();
        this.clientId = instagram.getClientIdd();
        this.clientSecret = instagram.getClientSecret();

        connect();
    }

    /**
     * Initialize share preferences
     */
    private void initPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    /**
     * First connect
     */
    private void connect() {

        // Verify token
        if (getToken() != null) {
            mListener.onConnect(this);
            return;
        }

        AuthenticationDialog authenticationDialog = AuthenticationDialog.newInstnace(mActivity, AUTHORIZATION_URL + "?client_id=" +  clientId + "&redirect_uri=" + clientCallback + "&response_type=code",
                clientCallback);
        authenticationDialog.addOnHttpCallback(new AuthenticationDialog.OnHttpCallback() {
            @Override
            public void onIntercept(WebView webView, String url) {
                onAuth(url);
            }
        });
        authenticationDialog.show();
    }

    /**
     * Make a oauth connection with authentication callback
     * @param url
     */
    private void onAuth(String url) {

        Uri uri = Uri.parse(url);
        String code = uri != null ? uri.getQueryParameter("code") : null;

        if (code != null && !code.isEmpty()) {

            requireToken(code);
            mCurrentStatus = STATUS.REQUIRING_TOKEN;
        } else {
            mCurrentStatus = STATUS.ERROR;

            mListener.onError(new ConnectionError(uri != null ? uri.getQueryParameter("error") : "Unknown error"));
        }

    }

    /**
     * Require access token
     * @param code
     */
    private void requireToken(String code) {

        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", clientCallback);
        params.put("code", code);

        this.mHttpClient.Build(OAUTH_URL, HttpMethod.POST, HttpUtils.getEncodedParams(params)).execute(new HttpCallback() {
            @Override
            public void onResponse(String body, int resultCode) {
                if (resultCode == 200) {
                    try {
                        JSONObject res = new JSONObject(body);
                        saveToken(res.getString("access_token"));

                        mCurrentStatus = STATUS.CONNECTED;
                        mListener.onConnect(InstagramSession.this);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mCurrentStatus = STATUS.ERROR;
                mListener.onError(new ConnectionError("Token error"));

            }

            @Override
            public void onFail(int resultCode) {
                mListener.onError(new ConnectionError("Http error: " + resultCode));
            }
        });

    }

    /**
     * Setun current token
     * @return
     */
    private String getToken() {
        return mSharedPreferences.getString(PREF_SHARED_TOKEN, null);
    }

    /**
     * Save token
     * @param access_token
     */
    private void saveToken(String access_token) {
        mSharedPreferences.edit().putString(PREF_SHARED_TOKEN, access_token).apply();
    }

    /**
     * Return current session status
     * @return
     */
    public STATUS getStatus() {
        return mCurrentStatus;
    }


    /**
     * Execute a instagram request
     * @param url
     * @param callback
     */
    public void execute(@NonNull String url, final RequestCallback callback) {

        this.mHttpClient.Build(ENDPOINT_URL + "/v1" + url + "?access_token=" + getToken(), HttpMethod.GET).execute(new HttpCallback() {
            @Override
            public void onResponse(String body, int resultCode) {
                callback.onResponse(resultCode, body);
            }

            @Override
            public void onFail(int resultCode) {
                callback.onResponse(resultCode, null);
            }
        });

    }

}
