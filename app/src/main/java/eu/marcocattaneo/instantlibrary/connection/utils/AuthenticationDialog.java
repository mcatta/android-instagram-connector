package eu.marcocattaneo.instantlibrary.connection.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class AuthenticationDialog extends Dialog {

    public static AuthenticationDialog newInstnace(Activity activity, String url, String urlCallback) {
        AuthenticationDialog f = new AuthenticationDialog(activity, url, urlCallback);
        return f;
    }

    public AuthenticationDialog(Activity activity, String url, String urlCallback) {
        super(activity);
        this.mActivity = activity;
        this.mUrl = url;
        this.mUrlCallback = urlCallback;
    }

    private String mUrl;

    private String mUrlCallback;

    private Activity mActivity;

    private OnHttpCallback mOnHttpCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView wv = new WebView(mActivity);
        wv.loadUrl(mUrl);
        wv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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

        int width = 0;
        int height = 0;
        Point size = new Point();
        WindowManager w = mActivity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            width = d.getWidth();
            height = d.getHeight();
        }


        setContentView(wv, new FrameLayout.LayoutParams(width, (int) (height * 0.6)));
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
