package com.tippler;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static String URL = "https://pe.onduline.com/";
    private WebView view;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (WebView) findViewById(R.id.webView);
        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);

        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        view.getSettings().setSupportMultipleWindows(false);
        view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        view.getSettings().setSupportZoom(false);
        view.getSettings().setUseWideViewPort(true);
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setAllowFileAccess(true);
        view.canGoBack();
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);
        if (savedInstanceState != null) {
            view.restoreState(savedInstanceState);
        } else {
            loadWebContent(URL);
        }
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (mySwipeRefreshLayout.isRefreshing()) {
                    mySwipeRefreshLayout.setRefreshing(false);
                    view.reload();
                }
            }
        });

        //Stop local links and redirects from opening in browser instead of webview
        view.setWebViewClient(new TipplerWebViewClient(this));


    }

    private void loadWebContent(String url) {
        Toast.makeText(this, "Loading Content Please Wait", Toast.LENGTH_SHORT).show();
        view.loadUrl(url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        view.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (view.copyBackForwardList().getCurrentIndex() >= 1) {
            Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_SHORT).show();
            view.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
