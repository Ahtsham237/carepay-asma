package com.example.carepay.retrofit;


import com.example.carepay.retrofitModel.retroModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("testing_stripe.php")
    Call<retroModel> stripeTokken(@Query("stripeToken") String action, @Query("rent") String rent);

}
