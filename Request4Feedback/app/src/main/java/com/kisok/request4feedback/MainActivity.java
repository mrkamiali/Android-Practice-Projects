package com.kisok.request4feedback;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kisok.request4feedback.client.WebViewClientClass;
import com.kisok.request4feedback.receiver.ConnectivityReceiver;

public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private WebView webView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    public static String URL = "https://www.request4feedback.com/dev/";
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;
    public static int counter = 0;
    private Snackbar snackbar;
    private ImageView imageView;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //keeps the screen on until app is running
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View v = this.findViewById(android.R.id.content);
        snackbar = Snackbar
                .make(v, "No Internet Connectivity !", Snackbar.LENGTH_INDEFINITE);
        webView = (WebView) findViewById(R.id.webView);
        mySwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeContainer);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
//        checkConnection();
        settingUpWebView();

        isConnected(this);
//        if (isConnected(this)) {
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            loadWebContent(URL);
        }
//        }
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mySwipeRefreshLayout.isRefreshing()) {
                    mySwipeRefreshLayout.setRefreshing(false);
                    imageView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    webView.reload();

//                        }
//                        else if (imageView.getVisibility() != View.VISIBLE) {
//                            imageView.setVisibility(View.GONE);
//                            webView.setVisibility(View.VISIBLE);
//                            loadWebContent(URL);
//
//                        }
                }
            }
        });

//Stop local links and redirects from opening in browser instead of webview
        webView.setWebViewClient(new WebViewClientClass(this, imageView, webView));
//        webView.saveState(savedInstanceState);
    }

    private void loadWebContent(String url) {
//        Toast.makeText(this, "Loading Content Please Wait", Toast.LENGTH_SHORT).show();
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void settingUpWebView() {
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setSupportMultipleWindows(false);
        //LayoutAlgorithms
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //Enables the cache
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setEnableSmoothTransition(true);
        webView.canGoBack();
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog(false);
            return false;
        }

    }

    public void showDialog(boolean isConnected) {

        builder = new AlertDialog.Builder(this);

        builder.setMessage("Please Connect your Device to wifi !")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (counter == 4) {
            MainActivity.this.finish();
        } else if (counter == 1) {
            counter++;
            Toast.makeText(MainActivity.this, "Tap 3 time to exit.", Toast.LENGTH_SHORT).show();
        } else {
            counter++;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Kami");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        imageView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

        Log.d(TAG, "onPostResume: Kami");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Kami");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Kami");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Kami");

//        if (isConnected(MainActivity.this)) {
        webView.reload();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Kami");
        counter = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Kami");
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnackbar(isConnected);
    }

    private void showSnackbar(boolean isConnected) {

//        int color = Color.WHITE;
//        View sbView = snackbar.getView();
//        sbView.setBackgroundColor(Color.parseColor("#CD5334"));
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(color);
//        boolean snackbarShown = !snackbar.isShown();
//
//        if (isConnected) {
//            if (snackbar.isShown()) {
//                snackbar.dismiss();
//            }
//        } else {
//            snackbar.show();
//        }

        if (!isConnected) {
            showDialog(isConnected);
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnackbar(isConnected);
//        showDialog(isConnected);
    }
}
