package eu.marcocattaneo.androidinstagramconnector.connection;

import android.app.Activity;

import android.support.annotation.NonNull;

import eu.marcocattaneo.androidinstagramconnector.connection.implementation.InstagramListener;

public class Instagram {

    private Activity mActivity;

    private String client_id;

    private String client_secret;

    private String client_callback;

    /**
     * To create an instance of Instagram APP
     * @param activity current activity
     * @param client_id client ID
     * @param client_secret client secret
     * @param callback authentication callback
     * @return
     */
    public static Instagram newInstance(Activity activity, @NonNull String client_id, @NonNull String client_secret, @NonNull String callback) {
        return new Instagram(activity, client_id, client_secret, callback);
    }

    /**
     * Constructor
     * @param activity current activity
     * @param client_id client ID
     * @param client_secret client secret
     * @param client_callback authentication callback
     */
    private Instagram(Activity activity, @NonNull String client_id, @NonNull String client_secret, @NonNull String client_callback) {

        this.client_id = client_id;
        this.client_secret = client_secret;
        this.client_callback = client_callback;

        this.mActivity = activity;
    }

    /**
     * Require session
     * @param instagramListener
     */
    public void getSession(InstagramListener instagramListener) {

        InstagramSession instagramSession = new InstagramSession(mActivity);
        instagramSession.loadSession(this, instagramListener);

    }

    /**
     * Return client ID
     * @return
     */
    public String getClientId() {
        return client_id;
    }

    /**
     * Return client secret
     * @return
     */
    public String getClientSecret() {
        return client_secret;
    }

    /**
     * Return authentication callback
     * @return
     */
    public String getClientCallback() {
        return client_callback;
    }
}
