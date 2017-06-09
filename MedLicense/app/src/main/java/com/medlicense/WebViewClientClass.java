package com.medlicense;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

/**
 * Created by Kamran ALi on 6/8/2017.
 */

public class WebViewClientClass extends WebViewClient {
    private Activity activity;
    String tel = "tel:770.456.5932";
    int prog = 10;


    public WebViewClientClass(MainActivity mainActivity) {
        this.activity = mainActivity;
    }


    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        String url = view.getOriginalUrl();
        Log.d("TAG", "onReceivedError:url "+url);
        Log.d("TAG", "onReceivedError:req "+request.getUrl().toString());
        if (request.getUrl().toString().equals(tel)){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(tel));
                activity.startActivity(intent);
                view.loadUrl("http://www.medlicense.com/");
            }else {
                showToast("Error While Loading Page...!");
                view.loadUrl("file:///android_asset/index.html");
            }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        if (url.equals(tel)){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(tel));
            activity.startActivity(intent);
            view.loadUrl("http://www.medlicense.com/");
        }
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);

    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
