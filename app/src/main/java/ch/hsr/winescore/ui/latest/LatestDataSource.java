package ch.hsr.winescore.ui.latest;

import android.support.annotation.NonNull;

import ch.hsr.winescore.data.api.GWSService;
import ch.hsr.winescore.data.api.responses.WineResponse;
import ch.hsr.winescore.ui.wine.WineDataSourceBase;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import retrofit2.Call;

public class LatestDataSource extends WineDataSourceBase {

    public LatestDataSource(GWSService apiService, DataLoadStateObserver observer) {
        super(apiService, observer);
    }

    @Override
    protected Call<WineResponse> getLoadInitialCall(@NonNull LoadInitialParams params) {
        return apiService.getLatest(params.pageSize, params.requestedStartPosition);
    }

    @Override
    protected Call<WineResponse> getLoadRangeCall(@NonNull LoadRangeParams params) {
        return apiService.getLatest(params.loadSize, params.startPosition);
    }
}