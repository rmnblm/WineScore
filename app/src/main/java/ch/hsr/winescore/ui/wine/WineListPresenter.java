package ch.hsr.winescore.ui.wine;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;

import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.domain.utils.DataLoadState;
import ch.hsr.winescore.ui.utils.LoadStateObservableFactory;
import ch.hsr.winescore.ui.utils.Presenter;

public abstract class WineListPresenter<T extends WineDataSourceBase> implements Presenter<WineListView> {

    private static final int PAGE_SIZE = 50;
    protected LiveData<PagedList<Wine>> wines;
    protected final LoadStateObservableFactory<T> dataSourceFactory;
    private final MutableLiveData<DataLoadState> loadState;
    private WineRecyclerViewAdapter adapter;

    public WineListPresenter(LoadStateObservableFactory<T> dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.loadState = new MutableLiveData<>();
        this.adapter = new WineRecyclerViewAdapter(this::listItemClicked);
    }

    public void attachView(WineListView view) {
        setupLiveWineData(dataSourceFactory);
        setupLoadStateObserver(dataSourceFactory);
    }

    protected abstract WineListView getView();

    private void setupLiveWineData(LoadStateObservableFactory dataSourceFactory) {
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE)
                        .build();

        wines = new LivePagedListBuilder(dataSourceFactory, config).build();
    }

    private void setupLoadStateObserver(LoadStateObservableFactory dataSourceFactory) {
        dataSourceFactory.setDataLoadStateObserver(this.loadState::postValue);

    }

    public void listItemClicked(View v, int position) {
        if (wines.getValue() == null) { return; }
        getView().navigateToDetailScreen(v, wines.getValue().get(position));
    }

    public void bindWines(LifecycleOwner owner) {
        wines.observe(owner, observedWines -> {
            adapter.submitList(observedWines);
            if (observedWines != null && observedWines.isEmpty()) {
                getView().showEmptyState();
            } else {
                getView().hideEmptyState();
            }
        });
    }

    public void bindLoadState(LifecycleOwner owner) {
        loadState.observe(owner, observedLoadState -> {
            switch (observedLoadState) {
                case INITIAL_LOADING:
                    getView().showLoading();
                    break;
                case LOADED:
                    getView().hideLoading();
                    break;
                case FAILED:
                    getView().showError(WineScoreApplication.getResourcesString(R.string.dataload_error_message));
                    break;
                default:
                    break;
            }
        });
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }

    public WineRecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
