package ch.hsr.winescore.ui.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import ch.hsr.winescore.data.api.GWSClient;
import ch.hsr.winescore.data.prefs.IPreferences;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.LoadStateObservableFactory;

public class SearchDataSourceFactory extends LoadStateObservableFactory<SearchDataSource> {

    public SearchDataSourceFactory(IPreferences preferences) {
        super(preferences);
    }

    @Override
    protected DataSource<Integer, Wine> createDataSource() {
        SearchDataSource dataSource = new SearchDataSource(GWSClient.getService(), observer, preferences);

        liveWineData = new MutableLiveData<>();
        liveWineData.postValue(dataSource);

        return dataSource;
    }
}
