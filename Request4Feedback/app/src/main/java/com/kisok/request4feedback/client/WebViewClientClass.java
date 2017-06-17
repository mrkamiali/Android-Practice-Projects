package com.kisok.request4feedback.client;

import android.app.Activity;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.kisok.request4feedback.MainActivity;

/**
 * Created by kamranali on 17/06/2017.
 */

public class WebViewClientClass extends WebViewClient {
    private Activity activity;
    private ImageView imageView;
    private WebView webView;

    public WebViewClientClass(MainActivity mainActivity, ImageView imageView, WebView webView) {
        this.activity = mainActivity;
        this.imageView = imageView;
        this.webView = webView;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        view.loadUrl("file:///android_asset/index.html");

    }


    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (MainActivity.counter != 0)
            MainActivity.counter = 0;
    }

    public WebViewClientClass() {
        super();
    }


    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (errorCode == -2) {
            imageView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
    }

}
