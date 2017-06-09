package com.kamran.onduline;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    public static String URL = "https://pe.onduline.com/";
    private WebView webView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    ProgressBar progressBar;
    TextView loadingText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        webView.setVisibility(View.GONE);
        loadingText = (TextView) findViewById(R.id.LoadingText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
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
        webView.setWebViewClient(new OndulineWebViewCLient(this));


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
//            Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_SHORT).show();
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    class OndulineWebViewCLient extends WebViewClient {
        private Activity activity;

        public OndulineWebViewCLient(MainActivity mainActivity) {
            this.activity = mainActivity;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            view.loadUrl("file:///android_asset/index.html");
        }


        private void showToast(String msg) {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
