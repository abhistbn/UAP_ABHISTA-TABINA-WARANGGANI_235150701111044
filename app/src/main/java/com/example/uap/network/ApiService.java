package com.example.uap.network;

import com.example.uap.model.Plant;
import com.example.uap.model.PlantResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("plant/all")
    Call<PlantResponse> getPlants();

    @GET("plant/{name}")
    Call<Plant> getPlant(@Path("name") String name);

    @POST("plant/new")
    Call<Plant> createPlant(@Body Plant plant);

    @PUT("plant/{name}")
    Call<Plant> updatePlant(@Path("name") String name, @Body Plant plant);

    @DELETE("plant/{name}")
    Call<Void> deletePlant(@Path("name") String name);
}