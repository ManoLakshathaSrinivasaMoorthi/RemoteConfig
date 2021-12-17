package com.example.dynamicdata.activities;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public  interface ApiEndUrl {
  @GET("carros/marcas")
  Call<List<Vehicles>> getbrands();
}
