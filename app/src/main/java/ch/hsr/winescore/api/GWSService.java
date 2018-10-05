package ch.hsr.winescore.api;

import ch.hsr.winescore.api.responses.WineResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GWSService {

    @GET("latest")
    Call<WineResponse> getLatest(
            @Query("limit") int limit,
            @Query("offset") int offset
    );

}