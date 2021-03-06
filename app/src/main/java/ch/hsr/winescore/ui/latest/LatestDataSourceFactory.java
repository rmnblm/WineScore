package ch.hsr.winescore.ui.latest;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import ch.hsr.winescore.data.api.GWSClient;
import ch.hsr.winescore.data.prefs.IPreferences;
import ch.hsr.winescore.ui.utils.LoadStateObservableFactory;
import ch.hsr.winescore.domain.models.Wine;

public class LatestDataSourceFactory extends LoadStateObservableFactory<LatestDataSource> {

    public LatestDataSourceFactory(IPreferences preferences) {
        super(preferences);
    }

    @Override
    protected DataSource<Integer, Wine> createDataSource() {
        LatestDataSource dataSource = new LatestDataSource(GWSClient.getService(), observer, preferences);

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }
}
