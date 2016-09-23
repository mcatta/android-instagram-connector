package eu.marcocattaneo.instantlibrary.connection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import eu.marcocattaneo.instantlibrary.connection.implementation.InstagramListener;
import eu.marcocattaneo.instantlibrary.connection.implementation.RequestCallback;
import eu.marcocattaneo.instantlibrary.connection.models.ConnectionError;

public class InstagramActivity extends Activity implements InstagramListener {

    InstagramSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = InstantLibrary.getSession(this);
        session.init("bf454118a3bf43408fcb63d7525df5c0", "988c001aeec04a27bf0531f66a039f4e", this);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        session.onAuth(intent);
    }

    @Override
    public void onConnect(InstagramSession session) {
        session.execute("/users/self", new RequestCallback() {
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
