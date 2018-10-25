package ch.hsr.winescore.ui.datasources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import ch.hsr.winescore.api.GWSClient;
import ch.hsr.winescore.utils.DataLoadStateObserver;
import ch.hsr.winescore.model.Wine;

public class LatestDataSourceFactory extends DataSource.Factory<Integer, Wine> {

    private MutableLiveData<LatestDataSource> liveWineData;
    private DataLoadStateObserver observer;

    public void setDataLoadStateObserver(DataLoadStateObserver observer) {
        this.observer = observer;
    }

    @Override
    public DataSource<Integer, Wine> create() {
        LatestDataSource dataSource = new LatestDataSource(GWSClient.getService(), observer);

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }

    public void invalidateDataSource() {
        liveWineData.getValue().invalidate();
    }
}
