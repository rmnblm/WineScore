package ch.hsr.winescore.data.api;

import ch.hsr.winescore.data.api.responses.WineResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GWSService {

    @GET("latest")
    Call<WineResponse> getLatest(
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET("latest")
    Call<WineResponse> searchBy(
            @Query("wine") String query,
            @Query("color") String color,
            @Query("country") String country,
            @Query("vintage") String vintage,
            @Query("ordering") String ordering,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

}