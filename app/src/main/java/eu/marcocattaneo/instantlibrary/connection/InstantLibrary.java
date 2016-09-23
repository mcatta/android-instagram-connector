package eu.marcocattaneo.instantlibrary.connection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import eu.marcocattaneo.instantlibrary.connection.client.HttpClient;
import eu.marcocattaneo.instantlibrary.connection.client.HttpMethod;
import eu.marcocattaneo.instantlibrary.connection.client.implementation.HttpCallback;
import eu.marcocattaneo.instantlibrary.connection.implementation.InstagramListener;
import eu.marcocattaneo.instantlibrary.connection.models.ConnectionError;
import eu.marcocattaneo.instantlibrary.connection.utils.HttpUtils;

public class InstantLibrary {

    private static InstagramSession mInstance;

    public enum STATUS {
        NOT_READY, AUTHORIZATION, CONNECTED, ERROR, REQUIRING_TOKEN
    }

    public static String OAUTH_CALLBACK = "http://instanlibrary/callback";

    public static InstagramSession getSession(@NonNull Activity activity) {

        if (mInstance == null)
            mInstance = new InstagramSession(activity);

        return mInstance;
    }
}
