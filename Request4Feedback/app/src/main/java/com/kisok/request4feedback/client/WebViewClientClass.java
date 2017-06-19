package com.kisok.request4feedback.client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

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
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (view.getUrl().equals(url+"/")){
            return true;
        } else if (view.getUrl().equals(url)){
            return true;
        }

        if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/about-us.php")) {
            showTaost("couldn't goto ABOUT_US section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://request4feedback.com/dev/about-us.php")) {
            showTaost("couldn't goto TEAM section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/profile/login.php")){
            return false;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/#pricing")){
            showTaost("couldn't goto PLAN Section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/profile/create_user.php")){
            return false;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/contact.php")){
            showTaost("couldn't goto CONTACT section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/#features")){
            showTaost("couldn't goto Features section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/#home")){
            showTaost("couldn't goto HOME Page");
            return true;
        }else if (Uri.parse(url).toString().equals("https://request4feedback.com/dev/#")){
            showTaost("couldn't goto HOME Page");
            return true;
        }else if (Uri.parse(url).toString().equals("https://request4feedback.com/dev/#home")){
            showTaost("couldn't goto HOME Page");
            return true;
        }else if (Uri.parse(url).toString().equals("https://request4feedback.com/dev/faq.php")){
            showTaost("couldn't goto HELP & Support section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/faq.php")){
            showTaost("couldn't goto FAQ section");
            return true;
        }else if (Uri.parse(url).toString().equals("https://www.request4feedback.com/dev/#home")){
            return false;
        }else if (Uri.parse(url).toString().equals("https://request4feedback.com/dev/privacy-policy.php")){
            showTaost("couldn't see Privacy and Policy");
            return true;
        }else if (Uri.parse(url).toString().equals("https://request4feedback.com/dev/terms-of-service.php")){
            showTaost("couldn't see Terms & Conditions");
            return true;
        }else if (Uri.parse(url).toString().equals("mailto:info@request4feedback.com")){
            showTaost("Couldn't email");
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }



    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
    }
}
