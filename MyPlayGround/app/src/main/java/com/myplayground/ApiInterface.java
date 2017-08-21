package com.myplayground;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("2")
    Call<ModelClass> getAtIndex2();



}