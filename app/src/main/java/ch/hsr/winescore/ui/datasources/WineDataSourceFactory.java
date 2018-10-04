package ch.hsr.winescore.ui.datasources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import ch.hsr.winescore.api.GWSService;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.DataLoadStateObserver;
import ch.hsr.winescore.model.Wine;

public class WineDataSourceFactory extends DataSource.Factory<Integer, Wine> {

    private MutableLiveData<WineDataSource> liveWineData;
    private MutableLiveData<DataLoadState> liveLoadState;
    private WineDataSource dataSource;
    private GWSService apiService;

    public WineDataSourceFactory(GWSService apiService) {
        this.apiService = apiService;
        this.liveLoadState = new MutableLiveData<>();
    }

    @Override
    public DataSource<Integer, Wine> create() {
        if (dataSource != null) {
            dataSource.setDataLoadStateObserver(null);
        }

        dataSource = new WineDataSource(apiService);
        dataSource.setDataLoadStateObserver(new DataLoadStateObserver() {
            @Override
            public void onDataLoadStateChanged(DataLoadState loadState) {
                liveLoadState.postValue(loadState);
            }
        });

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }

    public void invalidateDataSource() {
        liveWineData.getValue().invalidate();
    }

    public MutableLiveData<DataLoadState> getLoadState() {
        return liveLoadState;
    }
}
