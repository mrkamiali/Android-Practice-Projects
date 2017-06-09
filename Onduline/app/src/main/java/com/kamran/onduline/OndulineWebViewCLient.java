package com.kamran.onduline;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by kamranali on 27/04/2017.
 */

//class OndulineWebViewCLient extends WebViewClient {
//    private Activity activity;
//
//    public OndulineWebViewCLient(MainActivity mainActivity) {
//        this.activity = mainActivity;
//    }
//
//    @Override
//    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        super.onPageStarted(view, url, favicon);
//    }
//
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        return super.shouldOverrideUrlLoading(view, request);
//    }
//
//    @Override
//    public void onPageFinished(WebView view, String url) {
//        super.onPageFinished(view, url);
//        activity.findViewById(R.id.progressBar1).setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//        super.onReceivedError(view, request, error);
//        view.loadUrl("file:///android_asset/index.html");
//    }
//
//
//    private void showToast(String msg) {
//        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//    }
//}
