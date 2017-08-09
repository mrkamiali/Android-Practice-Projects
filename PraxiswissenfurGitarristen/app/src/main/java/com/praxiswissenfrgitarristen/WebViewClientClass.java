package com.praxiswissenfrgitarristen;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

/**
 * Created by kamranali on 09/08/2017.
 */

class WebViewClientClass extends WebViewClient {
    private Activity activity;
    private ImageView imageView, splashImageView;
    private WebView webView;
    private String lastUrl;
    public Showbar showbar;

    public WebViewClientClass(MainActivity mainActivity, ImageView imageView, WebView webView, ImageView splashScreen) {
        super();

        this.activity = mainActivity;
        this.imageView = imageView;
        this.webView = webView;
        this.splashImageView = splashScreen;
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
        if (splashImageView.getVisibility() == View.VISIBLE) {
            splashImageView.setVisibility(View.GONE);
        }
        if (showbar != null) {
            showbar.onstoploading();
        }
    }

    public interface Showbar {
        public void onstartloading();

        public void onstoploading();

    }
}

