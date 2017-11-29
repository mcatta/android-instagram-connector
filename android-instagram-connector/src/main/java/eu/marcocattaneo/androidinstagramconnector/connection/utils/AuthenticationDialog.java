package eu.marcocattaneo.androidinstagramconnector.connection.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import eu.marcocattaneo.androidinstagramconnector.R;

public class AuthenticationDialog extends Dialog {

    public static AuthenticationDialog newInstance(Activity activity, String url, String urlCallback) {
        return new AuthenticationDialog(activity, url, urlCallback);
    }

    private AuthenticationDialog(Activity activity, String url, String urlCallback) {
        super(activity);
        this.mUrl = url;
        this.mUrlCallback = urlCallback;
    }

    private String mUrl;

    private String mUrlCallback;

    private OnHttpCallback mOnHttpCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_authentication);

        WebView wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl(mUrl);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                if (mOnHttpCallback != null && url.startsWith(mUrlCallback)) {
                    dismiss();
                    mOnHttpCallback.onIntercept(view, url);
                }

                return false;
            }
        });
    }

    /**
     * Pass callback interface
     * @param onHttpCallback
     */
    public void addOnHttpCallback(OnHttpCallback onHttpCallback) {
        mOnHttpCallback = onHttpCallback;
    }

    public interface OnHttpCallback {

        void onIntercept(WebView webView, String url);

    }

}
