package ch.hsr.winescore.ui.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.api.GWSClient;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.WineDataSourceFactory;
import ch.hsr.winescore.ui.views.WineOverviewView;

public class WineOverviewPresenter implements Presenter<WineOverviewView> {

    private WineOverviewView view;
    private static final int PAGE_SIZE = 50;
    private LiveData<PagedList<Wine>> wines;
    private final WineDataSourceFactory dataSourceFactory;
    private final MutableLiveData<DataLoadState> loadState;

    public WineOverviewPresenter() {
        this.dataSourceFactory = new WineDataSourceFactory();
        this.loadState = new MutableLiveData<>();
    }

    @Override
    public void attachView(WineOverviewView view) {
        this.view = view;
        setupLiveWineData();
        setupLoadStateObserver();
    }

    private void setupLiveWineData() {
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE)
                        .build();

        wines = new LivePagedListBuilder(dataSourceFactory, config).build();
    }

    private void setupLoadStateObserver() {
        dataSourceFactory.setDataLoadStateObserver(this.loadState::postValue);
    }

    public LiveData<PagedList<Wine>> getWines() {
        return wines;
    }

    public MutableLiveData<DataLoadState> getLoadState() {
        return loadState;
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }

    public void listItemClicked(View v, int position) {
        view.navigateToDetailScreen(v, wines.getValue().get(position));
    }
}
