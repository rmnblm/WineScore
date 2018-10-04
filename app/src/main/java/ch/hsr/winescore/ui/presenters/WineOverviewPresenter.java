package ch.hsr.winescore.ui.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.WineDataSourceFactory;
import ch.hsr.winescore.ui.views.WineOverviewView;

public class WineOverviewPresenter implements Presenter<WineOverviewView> {

    private WineOverviewView view;
    private static final int PAGE_SIZE = 30;
    private LiveData<PagedList<Wine>> wines;
    private WineDataSourceFactory dataSourceFactory;


    public WineOverviewPresenter(WineDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public void attachView(WineOverviewView view) {
        this.view = view;

        setupWineLiveData();
    }

    private void setupWineLiveData() {
        wines = new LivePagedListBuilder(dataSourceFactory, PAGE_SIZE).build();
    }

    public LiveData<PagedList<Wine>> getWines() {
        return wines;
    }

    public MutableLiveData<DataLoadState> getLoadState() {
        return dataSourceFactory.getLoadState();
    }

    public void onRefresh() {
        dataSourceFactory.invalidateDataSource();
    }

    public void listItemClicked(View v, int position) {
        view.navigateToDetailScreen(v, wines.getValue().get(position));
    }
}
