package ch.hsr.winescore.ui.utils;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import ch.hsr.winescore.ui.wine.WineDataSourceBase;

public abstract class LoadStateObservableFactory<T extends WineDataSourceBase>
        extends DataSource.Factory<Integer, Wine> {

    protected MutableLiveData<T> liveWineData;
    protected DataLoadStateObserver observer;

    public void setDataLoadStateObserver(DataLoadStateObserver observer) {
        this.observer = observer;
    }

    @Override
    public DataSource<Integer, Wine> create() {
        return createDataSource();
    }


    public int getTotalCountOfCurrentDataSource() {
        return liveWineData.getValue() != null ? liveWineData.getValue().getTotalCount() : 0;
    }

    public void invalidateDataSource() {
        if (liveWineData != null && liveWineData.getValue() != null) {
            liveWineData.getValue().invalidate();
        }
    }

    protected abstract DataSource<Integer, Wine> createDataSource();
}
