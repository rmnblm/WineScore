package ch.hsr.winescore.ui.datasources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.DataLoadStateObserver;

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
        return liveWineData.getValue().getTotalCount();
    }

    public void invalidateDataSource() {
        liveWineData.getValue().invalidate();
    }

    protected abstract DataSource<Integer, Wine> createDataSource();
}
