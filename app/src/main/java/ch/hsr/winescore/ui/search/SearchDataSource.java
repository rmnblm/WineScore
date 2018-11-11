package ch.hsr.winescore.ui.search;

import android.support.annotation.NonNull;

import ch.hsr.winescore.data.api.GWSService;
import ch.hsr.winescore.data.api.responses.WineResponse;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import ch.hsr.winescore.ui.wine.WineDataSourceBase;
import retrofit2.Call;

public class SearchDataSource extends WineDataSourceBase {

    public SearchDataSource(GWSService apiService, DataLoadStateObserver observer) {
        super(apiService, observer);
    }

    @Override
    protected Call<WineResponse> getLoadInitialCall(@NonNull LoadInitialParams params) {
        refreshParameters();
        return apiService.searchBy(query, color, country, vintage, ordering, params.pageSize, 0);
    }

    @Override
    protected Call<WineResponse> getLoadRangeCall(@NonNull LoadRangeParams params) {
        return apiService.searchBy(query, color, country, vintage, ordering, params.loadSize, params.startPosition);
    }

}
