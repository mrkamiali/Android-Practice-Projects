package com.interhomesonline.client;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.interhomesonline.MainActivity;

/**
 * Created by kamranali on 05/07/2017.
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
        view.loadUrl(MainActivity.URL);

    }


    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);

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



    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

}
