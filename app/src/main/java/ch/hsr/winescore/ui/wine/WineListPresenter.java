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
    private final MutableLiveData<DataLoadState> loadState;
    private WineListView view;

    public WineListPresenter(LoadStateObservableFactory<T> dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.loadState = new MutableLiveData<>();
    }

    protected void attachView(WineListView view) {
        this.view = view;

        setupLiveWineData();
        setupLoadStateObserver();
    }

    protected abstract WineListView getView();

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

    public void listItemClicked(View v, int position) {
        if (wines.getValue() == null) { return; }
        getView().navigateToDetailScreen(v, wines.getValue().get(position));
    }

    public void bindWines(LifecycleOwner owner) {
        wines.observe(owner, wines -> view.winesUpdated(wines));
    }

    public void bindLoadState(LifecycleOwner owner) {
        loadState.observe(owner, loadState -> {
            if (loadState != null) {
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
            }
        });
    }

    public DataLoadState getLoadState() {
        return loadState.getValue();
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }
}
