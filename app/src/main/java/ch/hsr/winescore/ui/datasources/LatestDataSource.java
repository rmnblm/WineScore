package ch.hsr.winescore.ui.datasources;

import android.arch.paging.PositionalDataSource;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.io.IOException;

import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.api.GWSService;
import ch.hsr.winescore.api.responses.WineResponse;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.DataLoadStateObserver;
import retrofit2.Call;
import retrofit2.Response;

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
