package ch.hsr.winescore.ui.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.adapters.WineDataSourceFactory;
import ch.hsr.winescore.ui.views.WineOverviewView;

public class WineOverviewPresenter implements Presenter<WineOverviewView> {

    private WineOverviewView view;
    private static final int PAGE_SIZE = 30;
    private LiveData<PagedList<Wine>> wines;

    @Override
    public void attachView(WineOverviewView view) {
        this.view = view;

        setupWinesList();
    }

    private void setupWinesList() {
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setInitialLoadSizeHint(PAGE_SIZE)
                        .setPageSize(PAGE_SIZE)
                        .build();

        WineDataSourceFactory sourceFactory = new WineDataSourceFactory();

        wines = new LivePagedListBuilder(sourceFactory, pagedListConfig).build();
    }

    public LiveData<PagedList<Wine>> getWines() {
        return wines;
    }

    public void listItemClicked(View v, int position) {
        view.navigateToDetailScreen(v, wines.getValue().get(position));
    }
}
