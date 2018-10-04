package ch.hsr.winescore.ui.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.WineDataSourceFactory;
import ch.hsr.winescore.ui.views.WineOverviewView;

public class WineOverviewPresenter implements Presenter<WineOverviewView> {

    private WineOverviewView view;
    private static final int PAGE_SIZE = 20;
    private LiveData<PagedList<Wine>> wines;
    private WineDataSourceFactory dataSourceFactory;

    public WineOverviewPresenter(WineDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
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
        dataSourceFactory.setDataLoadStateObserver(loadState -> {
            switch (loadState) {
                case INITIAL_LOADING:
                    view.showLoading();
                    break;
                case LOADED:
                    view.hideLoading();
                    break;
                case FAILED:
                    view.showError("An error occured. Try to reload."); // TODO: Localize
                    break;
            }
        });
    }

    public LiveData<PagedList<Wine>> getWines() {
        return wines;
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }

    public void listItemClicked(View v, int position) {
        view.navigateToDetailScreen(v, wines.getValue().get(position));
    }
}
