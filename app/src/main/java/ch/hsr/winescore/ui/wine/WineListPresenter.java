package ch.hsr.winescore.ui.wine;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.domain.utils.DataLoadState;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.LoadStateObservableFactory;

public abstract class WineListPresenter<T extends WineDataSourceBase> {

    private static final int PAGE_SIZE = 50;
    protected LiveData<PagedList<Wine>> wines;
    protected final LoadStateObservableFactory<T> dataSourceFactory;
    protected final MutableLiveData<DataLoadState> loadState;
    protected WineRecyclerViewAdapter adapter;

    public WineListPresenter(LoadStateObservableFactory<T> dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.loadState = new MutableLiveData<>();
        this.adapter = new WineRecyclerViewAdapter(this::listItemClicked);
    }

    protected void setupLiveWineData(LoadStateObservableFactory dataSourceFactory) {
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE)
                        .build();

        wines = new LivePagedListBuilder(dataSourceFactory, config).build();
    }

    protected void setupLoadStateObserver(LoadStateObservableFactory dataSourceFactory) {
        dataSourceFactory.setDataLoadStateObserver(this.loadState::postValue);

    }

    public void listItemClicked(View v, int position) {
        getView().navigateToDetailScreen(v, wines.getValue().get(position));
    }

    public void bindWines(LifecycleOwner owner) {
        wines.observe(owner, wines -> adapter.submitList(wines));
    }

    public void bindLoadState(LifecycleOwner owner) {
        loadState.observe(owner, loadState -> {
            switch (loadState) {
                case INITIAL_LOADING:
                    getView().showLoading();
                    break;
                case LOADED:
                    getView().hideLoading();
                    break;
                case FAILED:
                    getView().showError(WineScoreApplication.getResourcesString(R.string.dataload_error_message));
                    break;
            }
        });
    }

    public WineRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    protected abstract WineListView getView();
}
