package eu.marcocattaneo.androidinstagramconnector.connection;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import eu.marcocattaneo.androidinstagramconnector.connection.client.HttpClient;
import eu.marcocattaneo.androidinstagramconnector.connection.client.HttpMethod;
import eu.marcocattaneo.androidinstagramconnector.connection.client.implementation.HttpCallback;
import eu.marcocattaneo.androidinstagramconnector.connection.implementation.InstagramListener;
import eu.marcocattaneo.androidinstagramconnector.connection.implementation.RequestCallback;
import eu.marcocattaneo.androidinstagramconnector.connection.models.ConnectionError;
import eu.marcocattaneo.androidinstagramconnector.connection.models.Scope;
import eu.marcocattaneo.androidinstagramconnector.connection.utils.AuthenticationDialog;
import eu.marcocattaneo.androidinstagramconnector.connection.utils.HttpUtils;

public class InstagramSession {

    public enum STATUS {
        NOT_READY, REQUIRING_AUTHORIZATION, CONNECTED, ERROR, REQUIRING_TOKEN
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

    private Set<Scope> mScopes;

    public InstagramSession(@NonNull Activity activity, Set<Scope> scopes) {
        this.mActivity = activity;

        this.mScopes = scopes;

        initPreferences();
    }

    public void loadSession(Instagram instagram, InstagramListener instagramListener) {

        this.mListener = instagramListener;
        this.mHttpClient = new HttpClient(mActivity);

        this.clientCallback = instagram.getClientCallback();
        this.clientId = instagram.getClientId();
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
            mCurrentStatus = STATUS.CONNECTED;
            mListener.onConnect(this);
            return;
        }

        // Scope
        String stringScope = "";
        if (mScopes != null && mScopes.size() > 0) {
            stringScope = "&scope=";
            for (Scope scope : mScopes) {

                stringScope += scope.getScopeValue() + "+";
            }
            stringScope = stringScope.substring(0, stringScope.length()-1);
        }

        mCurrentStatus = STATUS.REQUIRING_AUTHORIZATION;

        AuthenticationDialog authenticationDialog = AuthenticationDialog.newInstnace(mActivity, AUTHORIZATION_URL + "?client_id=" +  clientId + "&redirect_uri=" + clientCallback + "&response_type=code" + stringScope,
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
                //TODO manage errors
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
    public String getToken() {
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
     * Execute an instagram request (standard GET)
     * @param url
     * @param callback
     */
    public void execute(@NonNull String url, final RequestCallback callback) {

        execute(url, HttpMethod.GET, null, callback);

    }

    /**
     * Execute an instagram request with parameters
     * @param url
     * @param method
     * @param body
     * @param callback
     */
    public void execute(@NonNull String url, HttpMethod method, String body, final RequestCallback callback) {

        String token = url.contains("?") ? "&access_token=" : "?access_token=";
        token += getToken();

        this.mHttpClient.Build(ENDPOINT_URL + "/v1" + url + token, method, body).execute(new HttpCallback() {
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