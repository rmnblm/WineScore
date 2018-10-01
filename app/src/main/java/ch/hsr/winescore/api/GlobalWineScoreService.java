package ch.hsr.winescore.api;

import ch.hsr.winescore.model.WineList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GlobalWineScoreService {

    @GET("latest")
    Call<WineList> getLatest(
            @Query("limit") int limit,
            @Query("offset") int offset
    );

}