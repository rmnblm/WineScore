package ch.hsr.winescore.ui.datasources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import ch.hsr.winescore.api.GWSClient;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.DataLoadStateObserver;

public class SearchDataSourceFactory extends LoadStateObservableFactory<SearchDataSource> {

    @Override
    protected DataSource<Integer, Wine> createDataSource() {
        SearchDataSource dataSource = new SearchDataSource(GWSClient.getService(), observer);

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }
}
