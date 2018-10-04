package ch.hsr.winescore.ui.datasources;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import ch.hsr.winescore.api.GWSService;
import ch.hsr.winescore.api.responses.WineResponse;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.DataLoadStateObserver;
import ch.hsr.winescore.model.Wine;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class WineDataSource extends PositionalDataSource<Wine> {

    private final GWSService apiService;
    private final DataLoadStateObserver observer;

    public WineDataSource(GWSService apiService, DataLoadStateObserver observer) {
        this.apiService = apiService;
        this.observer = observer;
    }

    public WineDataSource(GWSService apiService) {
        this(apiService, loadState -> {
            // Null object, do nothing
        });
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.INITIAL_LOADING);

        System.out.println("[loadInitial] pageSize = " + params.pageSize +
                ", requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize);

        final Call<WineResponse> wineListCall = apiService.getLatest(params.pageSize, params.requestedStartPosition);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            callback.onResult(response.body().getWines(), params.requestedStartPosition, response.body().getCount());
            observer.onDataLoadStateChanged(DataLoadState.INITIAL_LOADED);
        } catch (IOException e) {
            e.printStackTrace();
            observer.onDataLoadStateChanged(DataLoadState.FAILED);
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.LOADING);

        System.out.println("[loadRange] loadSize = " + params.loadSize + ", startPosition = " + params.startPosition);

        final Call<WineResponse> wineListCall = apiService.getLatest(params.loadSize, params.startPosition);

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
