package com.myplayground;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    //        String url = "https://letusintroduceyou.com/Apis/Site/Login"/\;
//    String url = "https://api.androidhive.info/volley/person_object.json";
    // Tag used to cancel the request
    String tag_json_obj = "json_obj_req";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ModelClass> call =  apiService.getAtIndex2();
        call.enqueue(new Callback<ModelClass>() {
            @Override
            public void onResponse(retrofit2.Call<ModelClass> call, retrofit2.Response<ModelClass> response) {
                Log.d(TAG, "onResponse: "+response.body().getId());
            }

            @Override
            public void onFailure(retrofit2.Call<ModelClass> call, Throwable t) {

            }
        });
//        JsonObjectRequestFunc();
//        JsonArrayRequestFunc();
//        postRequestWithParams();
//        postStringRequest();
        //for loading image into networkImageView
//        ImageLoaderFunc();
        //Loading image in ImageView
//        ImageLoadingFunc();
        // placeholder image and error image
//        loadImageWithError();
    }

    private void postStringRequest() {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        String url = "https://letusintroduceyou.com/Apis/Site/Login";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: r" + response.toString());
                pDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("LoginForm[password]", "123");
                params.put("mob", "true");
                params.put("LoginForm[username]", "daniyal@gmail.com");

                return params;

            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void loadImageWithError() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        // Loading image with placeholder and error image
//        imageLoader.get("URL_IMAGE", ImageLoader.getImageListener(
//              "IAMGEVIEW", R.mipmap.ic_launcher, R.mipmap.ic_launcher));
    }

    private void ImageLoadingFunc() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

// If you are using normal ImageView
        imageLoader.get("URL_IMAGE", new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
//                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    private void ImageLoaderFunc() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

// If you are using NetworkImageView
//        imgNetWorkView.setImageUrl(Const.URL_IMAGE, imageLoader);
    }

    private void postRequestWithParams() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "https://letusintroduceyou.com/Apis/Site/ServiceProvidersListing";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
//                url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                pDialog.hide();
//                Log.d(TAG, "onResponse: " + response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                pDialog.hide();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_type", "ServiceProvider");
//                params.put("user_id", "b49313113770134c54ccc0945b");
//
//                return params;
//            }
//        };
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pDialog.hide();
                        Log.d(TAG, "onResponse: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_type", "ServiceProvider");
                params.put("user_id", "b49313113770134c54ccc0945b");

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }


    private void JsonArrayRequestFunc() {
        // Tag used to cancel the request
        String tag_json_arry = "json_array_req";

        String url = "https://api.androidhive.info/volley/person_array.json";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: " + response.toString());
                pDialog.hide();
                try {
                    JSONArray jsonArray = response;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject phone = jsonObject.getJSONObject("phone");

                        Log.d(TAG, "onResponse: " + jsonObject.get("name"));
                        Log.d(TAG, "onResponse: " + phone.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, " onError " + error.toString());
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(request, tag_json_arry);
    }

    private void JsonObjectRequestFunc() {
        String url = "https://api.androidhive.info/volley/person_object.json";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
                try {
                    Log.d(TAG, "onResponse: " + response.get("name").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, tag_json_obj);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        AppController.getInstance().getRequestQueue().getCache().clear();
        super.onDestroy();
    }
}
