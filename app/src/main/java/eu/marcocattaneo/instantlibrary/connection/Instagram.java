package eu.marcocattaneo.instantlibrary.connection;

import android.app.Activity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import eu.marcocattaneo.instantlibrary.connection.implementation.InstagramListener;

public class Instagram {

    private Activity mActivity;

    private String client_id;

    private String client_secret;

    private String client_callback;

    public static Instagram newInstance(Activity activity, @NonNull String client_id, @NonNull String client_secret, @NonNull String callback) {
        return new Instagram(activity, client_id, client_secret, callback);
    }

    private Instagram(Activity activity, @NonNull String client_id, @NonNull String client_secret, @NonNull String client_callback) {

        this.client_id = client_id;
        this.client_secret = client_secret;
        this.client_callback = client_callback;

        this.mActivity = activity;
    }

    public void getSession(InstagramListener instagramListener) {

        InstagramSession instagramSession = new InstagramSession(mActivity);
        instagramSession.loadSession(this, instagramListener);

    }

    public String getClientIdd() {
        return client_id;
    }

    public String getClientSecret() {
        return client_secret;
    }

    public String getClientCallback() {
        return client_callback;
    }
}
