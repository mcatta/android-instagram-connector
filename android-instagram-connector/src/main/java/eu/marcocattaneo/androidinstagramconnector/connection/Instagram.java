package eu.marcocattaneo.androidinstagramconnector.connection;

import android.app.Activity;

import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.marcocattaneo.androidinstagramconnector.connection.implementation.InstagramListener;
import eu.marcocattaneo.androidinstagramconnector.connection.models.Scope;

public class Instagram {

    private Activity mActivity;

    private String client_id;

    private String client_secret;

    private String client_callback;

    private Set<Scope> mScopes;

    /**
     * To create an instance of Instagram APP
     * @param activity current activity
     * @param client_id client ID
     * @param client_secret client secret
     * @param callback authentication callback
     * @return Instagram instance
     */
    public static Instagram newInstance(Activity activity, @NonNull String client_id, @NonNull String client_secret, @NonNull String callback) {
        return new Instagram(activity, client_id, client_secret, callback);
    }

    /**
     * Add sigle scope
     * @param scope
     * @return Instagram instance
     */
    public Instagram addScope(Scope scope) {
        mScopes.add(scope);

        return this;
    }

    /**
     * Add multiple scopes
     * @param scopes set
     * @return Instagram instance
     */
    public Instagram addScopes(Set<Scope> scopes) {
        mScopes = scopes;

        return this;
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

        this.mScopes = new HashSet<>();

        this.mActivity = activity;
    }

    /**
     * Require session
     * @param instagramListener
     */
    public void getSession(InstagramListener instagramListener) {

        InstagramSession instagramSession = new InstagramSession(mActivity, mScopes);
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
