package ch.hsr.winescore.ui.datasources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import ch.hsr.winescore.api.GWSService;
import ch.hsr.winescore.utils.DataLoadStateObserver;
import ch.hsr.winescore.model.Wine;

public class WineDataSourceFactory extends DataSource.Factory<Integer, Wine> {

    private MutableLiveData<WineDataSource> liveWineData;
    private DataLoadStateObserver observer;
    private WineDataSource dataSource;
    private GWSService apiService;

    public WineDataSourceFactory(GWSService apiService) {
        this.apiService = apiService;
    }

    public void setDataLoadStateObserver(DataLoadStateObserver observer) {
        this.observer = observer;
    }

    @Override
    public DataSource<Integer, Wine> create() {
        dataSource = new WineDataSource(apiService, observer);

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }

    public void invalidateDataSource() {
        liveWineData.getValue().invalidate();
    }
}
