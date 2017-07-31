package com.uronlinemart;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kamran ALi on 7/30/2017.
 */

class WebViewClientClass extends WebViewClient {
    private Activity activity;
    private ImageView imageView;
    private WebView webView;
    private String lastUrl;
    public Showbar showbar;
    private TextView appTitle;

    public WebViewClientClass(MainActivity mainActivity, ImageView imageView, WebView webView,TextView textView) {
        super();
        this.appTitle = textView;
        this.activity = mainActivity;
        this.imageView = imageView;
        this.webView = webView;
        showbar = (Showbar) mainActivity;
    }

    public void reload() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                webView.loadUrl(lastUrl);
                imageView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        }, 1000);

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        view.loadUrl("file:///android_asset/errorpage.html");
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (showbar != null) {
            showbar.onstoploading();
        }
        imageView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        lastUrl = failingUrl;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        appTitle.setText(view.getTitle());
        if (showbar != null) {
            showbar.onstoploading();
        }
    }

    public interface Showbar {
        public void onstartloading();

        public void onstoploading();

    }
}

