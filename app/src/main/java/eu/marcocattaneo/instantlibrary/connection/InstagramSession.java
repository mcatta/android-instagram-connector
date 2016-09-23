package eu.marcocattaneo.instantlibrary.connection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import eu.marcocattaneo.instantlibrary.connection.client.HttpClient;
import eu.marcocattaneo.instantlibrary.connection.client.HttpMethod;
import eu.marcocattaneo.instantlibrary.connection.client.implementation.HttpCallback;
import eu.marcocattaneo.instantlibrary.connection.implementation.InstagramListener;
import eu.marcocattaneo.instantlibrary.connection.implementation.RequestCallback;
import eu.marcocattaneo.instantlibrary.connection.models.ConnectionError;
import eu.marcocattaneo.instantlibrary.connection.utils.HttpUtils;

public class InstagramSession {

    private static final String PREF_SHARED_TOKEN = "InstantLibrary:saved_token";

    private static final String PREF_SHARED_CLIENT_ID = "InstantLibrary:client_id";

    private static final String PREF_SHARED_CLIENT_SECRET = "InstantLibrary:client_secret";

    private static final String ENDPOINT_URL = "https://api.instagram.com";

    private static final String AUTHORIZATION_URL = ENDPOINT_URL + "/oauth/authorize/";

    private static final String OAUTH_URL = ENDPOINT_URL + "/oauth/access_token";

    private InstantLibrary.STATUS mCurrentStatus = InstantLibrary.STATUS.NOT_READY;

    private InstagramListener mListener;

    private SharedPreferences mSharedPreferences;

    private Activity mActivity;

    private HttpClient mHttpClient;

    public InstagramSession(@NonNull Activity activity) {
        this.mActivity = activity;
        initPreferences();
    }

    /**
     * Inizialize shared preferences
     */
    private void initPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    /**
     * Set and save token in shared preferences
     * @param token token to save
     */
    private void saveToken(String token) {
        mSharedPreferences.edit().putString(PREF_SHARED_TOKEN, token).apply();
    }

    /**
     * Return saved token
     * @return String token
     */
    private String getToken() {
        return mSharedPreferences.getString(PREF_SHARED_TOKEN, null);
    }

    /**
     * Initialize library
     * @param client_id
     * @param client_secret
     * @param instagramListener
     */
    public void init(@NonNull String client_id, @NonNull String client_secret, @NonNull InstagramListener instagramListener) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PREF_SHARED_CLIENT_ID, client_id);
        editor.putString(PREF_SHARED_CLIENT_SECRET, client_secret);
        editor.apply();

        this.mListener = instagramListener;
        this.mHttpClient = new HttpClient(mActivity);

        connect();
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

        mCurrentStatus = InstantLibrary.STATUS.AUTHORIZATION;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AUTHORIZATION_URL + "?client_id=" +  mSharedPreferences.getString(PREF_SHARED_CLIENT_ID, "") + "&redirect_uri=" + InstantLibrary.OAUTH_CALLBACK + "&response_type=code"));
        mActivity.startActivity(i);
    }

    /**
     * Make a oauth connection with authentication callback
     * @param intent
     */
    public void onAuth(Intent intent) {

        Uri uri = intent.getData();
        String code = uri != null ? uri.getQueryParameter("code") : null;

        if (code != null && !code.isEmpty()) {

            requireToken(code);
            mCurrentStatus = InstantLibrary.STATUS.REQUIRING_TOKEN;
        } else {
            mCurrentStatus = InstantLibrary.STATUS.ERROR;

            mListener.onError(new ConnectionError(uri != null ? uri.getQueryParameter("error") : "Unknown error"));
        }

    }

    /**
     * Require access token
     * @param code
     */
    private void requireToken(String code) {

        Map<String, String> params = new HashMap<>();
        params.put("client_id", mSharedPreferences.getString(PREF_SHARED_CLIENT_ID, ""));
        params.put("client_secret", mSharedPreferences.getString(PREF_SHARED_CLIENT_SECRET, ""));
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", InstantLibrary.OAUTH_CALLBACK);
        params.put("code", code);

        this.mHttpClient.Build(OAUTH_URL, HttpMethod.POST, HttpUtils.getEncodedParams(params)).execute(new HttpCallback() {
            @Override
            public void onResponse(String body, int resultCode) {
                if (resultCode == 200) {
                    try {
                        JSONObject res = new JSONObject(body);
                        saveToken(res.getString("access_token"));

                        mCurrentStatus = InstantLibrary.STATUS.CONNECTED;
                        mListener.onConnect(InstagramSession.this);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mCurrentStatus = InstantLibrary.STATUS.ERROR;
                mListener.onError(new ConnectionError("Token error"));

            }

            @Override
            public void onFail(int resultCode) {
                mListener.onError(new ConnectionError("Http error: " + resultCode));
            }
        });

    }

    /**
     * Return current session status
     * @return
     */
    public InstantLibrary.STATUS getStatus() {
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
