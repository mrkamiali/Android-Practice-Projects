package com.kisok.request4feedback.client;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.kisok.request4feedback.MainActivity;
import com.kisok.request4feedback.Util;

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

    private void showTaost(String s) {
        Toast.makeText(activity,s+", you are in Kiosk Mode!",Toast.LENGTH_SHORT).show();
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
        if (view.getUrl().equals(url+"/")){
            return true;
        } else if (view.getUrl().equals(url)){
            return true;
        }

        if (Uri.parse(url).toString().equals(Util.ABOUTUS)) {
            showTaost("couldn't goto ABOUT_US section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.ABOUT_US)) {
            showTaost("couldn't goto TEAM section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.LOGIN)){
            return false;
        }else if (Uri.parse(url).toString().equals(Util.PRICING)){
            showTaost("couldn't goto PLAN Section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.CREATE_USER)){
            return false;
        }else if (Uri.parse(url).toString().equals(Util.CONTACT)){
            showTaost("couldn't goto CONTACT section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.FEATURES)){
            showTaost("couldn't goto Features section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.HOME)){
            showTaost("couldn't goto HOME Page");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.HOME_1)){
            showTaost("couldn't goto HOME Page");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.HOME_2)){
            showTaost("couldn't goto HOME Page");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.FAQ)){
            showTaost("couldn't goto HELP & Support section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.FAQ_1)){
            showTaost("couldn't goto FAQ section");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.HOME_3)){
            return false;
        }else if (Uri.parse(url).toString().equals(Util.PRIVACY_POLICY)){
            showTaost("couldn't see Privacy and Policy");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.TERMS_OF_SERVICE)){
            showTaost("couldn't see Terms & Conditions");
            return true;
        }else if (Uri.parse(url).toString().equals(Util.MAIL_TO)){
            showTaost("Couldn't email");
            return true;
        }
        return false;
    }

}
