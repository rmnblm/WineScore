package ch.hsr.winescore.ui.datasources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import ch.hsr.winescore.api.GWSClient;
import ch.hsr.winescore.utils.DataLoadStateObserver;
import ch.hsr.winescore.model.Wine;

public class LatestDataSourceFactory extends LoadStateObservableFactory<LatestDataSource> {

    @Override
    protected DataSource<Integer, Wine> createDataSource() {
        LatestDataSource dataSource = new LatestDataSource(GWSClient.getService(), observer);

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }
}
