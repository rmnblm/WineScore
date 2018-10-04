package ch.hsr.winescore.ui.adapters;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import ch.hsr.winescore.api.GWSApi;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.api.responses.WineResponse;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class WinePositionalDataSource extends PositionalDataSource<Wine> {

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Wine> callback) {
        final Call<WineResponse> wineListCall = GWSApi
                .getService()
                .getLatest(params.pageSize, params.requestedStartPosition);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            callback.onResult(response.body().getWines(), params.requestedStartPosition, response.body().getCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Wine> callback) {
        final Call<WineResponse> wineListCall = GWSApi
                .getService()
                .getLatest(params.loadSize, params.startPosition);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            callback.onResult(response.body().getWines());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
