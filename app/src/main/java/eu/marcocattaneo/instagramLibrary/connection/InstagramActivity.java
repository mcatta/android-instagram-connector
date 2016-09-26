package eu.marcocattaneo.instagramLibrary.connection;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import eu.marcocattaneo.instagramLibrary.connection.implementation.InstagramListener;
import eu.marcocattaneo.instagramLibrary.connection.implementation.RequestCallback;
import eu.marcocattaneo.instagramLibrary.connection.models.ConnectionError;

public class InstagramActivity extends Activity implements InstagramListener {

    Instagram instagram;

    InstagramSession session = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instagram = Instagram.newInstance(this, "bf454118a3bf43408fcb63d7525df5c0", "988c001aeec04a27bf0531f66a039f4e", "http://instanlibrary/callback");
        instagram.getSession(this);
    }

    @Override
    public void onConnect(InstagramSession session) {
        this.session = session;
        this.session.execute("/users/self", new RequestCallback() {
            @Override
            public void onResponse(int resultCode, @Nullable String body) {
                Toast.makeText(InstagramActivity.this, body, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(ConnectionError error) {

    }
}
