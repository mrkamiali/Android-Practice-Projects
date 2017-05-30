package com.kamran.onduline;

import android.app.Activity;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by kamranali on 27/04/2017.
 */

class OndulineWebViewCLient extends WebViewClient {
    private Activity activity;

    public OndulineWebViewCLient(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        showToast("Error While Loading Page...!");
        view.loadUrl("file:///android_asset/index.html");
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
