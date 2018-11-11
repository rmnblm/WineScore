package ch.hsr.winescore.ui.latest;

import android.support.annotation.NonNull;

import ch.hsr.winescore.data.api.GWSService;
import ch.hsr.winescore.data.api.responses.WineResponse;
import ch.hsr.winescore.data.prefs.IPreferences;
import ch.hsr.winescore.ui.wine.WineDataSourceBase;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import retrofit2.Call;

public class LatestDataSource extends WineDataSourceBase {

    public LatestDataSource(GWSService apiService, DataLoadStateObserver observer, IPreferences preferences) {
        super(apiService, observer, preferences);
    }

    @Override
    protected Call<WineResponse> getLoadInitialCall(@NonNull LoadInitialParams params) {
        refreshParameters();
        return apiService.getLatest(color, country, vintage, ordering, params.pageSize, 0);
    }

    @Override
    protected Call<WineResponse> getLoadRangeCall(@NonNull LoadRangeParams params) {
        return apiService.getLatest(color, country, vintage, ordering, params.loadSize, params.startPosition);
    }
}
