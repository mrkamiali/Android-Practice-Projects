package com.medlicense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity  extends Activity  {
    String tel = "tel:770.456.5932";
    public static String URL = "http://www.medlicense.com/";
    private WebView webView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    Button callUs ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.canGoBack();
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
//        webView.addJavascriptInterface(jsInterface,"Call Us");
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            loadWebContent(URL);
        }
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (mySwipeRefreshLayout.isRefreshing()) {
                    mySwipeRefreshLayout.setRefreshing(false);
                    webView.reload();
                }
            }
        });

        //Stop local links and redirects from opening in browser instead of webview
        webView.setWebViewClient(new WebViewClientClass(this));


    }

    private void loadWebContent(String url) {
//        Toast.makeText(this, "Loading Content Please Wait", Toast.LENGTH_SHORT).show();
        webView.loadUrl(url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (webView.copyBackForwardList().getCurrentIndex() >= 1) {
            Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
