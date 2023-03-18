package com.example.moeen;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChatCompletionsAPI {
    @POST("chat/completions")
    Call<ResponseBody> getChatCompletions(@Header("header1") String header1, @Header("header2") String header2, @Body ChatCompletionsRequest request);
}
