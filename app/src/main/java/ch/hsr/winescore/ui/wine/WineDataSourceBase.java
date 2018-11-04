package ch.hsr.winescore.ui.wine;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import ch.hsr.winescore.data.api.GWSService;
import ch.hsr.winescore.data.api.responses.WineResponse;
import ch.hsr.winescore.domain.utils.DataLoadState;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public abstract class WineDataSourceBase extends PositionalDataSource<Wine> {

    protected final GWSService apiService;
    protected final DataLoadStateObserver observer;

    private int totalCount = 0;

    public WineDataSourceBase(GWSService apiService, DataLoadStateObserver observer) {
        this.apiService = apiService;
        this.observer = observer;
    }

    protected abstract Call<WineResponse> getLoadInitialCall(@NonNull LoadInitialParams params);
    protected abstract Call<WineResponse> getLoadRangeCall(@NonNull LoadRangeParams params);

    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.INITIAL_LOADING);

        final Call<WineResponse> wineListCall = getLoadInitialCall(params);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            totalCount = response.body().getCount();
            callback.onResult(response.body().getWines(), 0, totalCount);
            observer.onDataLoadStateChanged(DataLoadState.LOADED);
        } catch (IOException e) {
            e.printStackTrace();
            observer.onDataLoadStateChanged(DataLoadState.FAILED);
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.LOADING);

        final Call<WineResponse> wineListCall = getLoadRangeCall(params);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            callback.onResult(response.body().getWines());
            observer.onDataLoadStateChanged(DataLoadState.LOADED);
        } catch (IOException e) {
            e.printStackTrace();
            observer.onDataLoadStateChanged(DataLoadState.FAILED);
        }
    }

}
