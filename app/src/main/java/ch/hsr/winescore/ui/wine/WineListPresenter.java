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
import ch.hsr.winescore.ui.utils.ListView;
import ch.hsr.winescore.ui.utils.LoadStateObservableFactory;
import ch.hsr.winescore.ui.utils.Presenter;

public abstract class WineListPresenter<T extends WineDataSourceBase> implements Presenter<ListView<Wine>> {

    private static final int PAGE_SIZE = 50;
    protected LiveData<PagedList<Wine>> wines;
    protected final LoadStateObservableFactory<T> dataSourceFactory;
    private final MutableLiveData<DataLoadState> loadState;

    private ListView<Wine> view;

    public WineListPresenter(LoadStateObservableFactory<T> dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.loadState = new MutableLiveData<>();
    }

    public void attachView(ListView<Wine> view) {
        this.view = view;

        setupLiveWineData();
        setupLoadStateObserver();
    }

    protected abstract ListView<Wine> getView();

    private void setupLiveWineData() {
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE)
                        .build();

        wines = new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    private void setupLoadStateObserver() {
        dataSourceFactory.setDataLoadStateObserver(this.loadState::postValue);

    }

    public void listItemClicked(View v, int position) {
        if (wines.getValue() == null) { return; }
        getView().navigateToDetailScreen(v, wines.getValue().get(position));
    }

    public void bindWines(LifecycleOwner owner) {
        wines.observe(owner, observedWines -> {
            getView().winesUpdated(observedWines);
        });
    }

    public void bindLoadState(LifecycleOwner owner) {
        loadState.observe(owner, observedLoadState -> {
            if (observedLoadState != null) {
                refreshEmptyState();
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
            }
        });
    }

    private void refreshEmptyState() {
        if (wines.getValue() != null && wines.getValue().isEmpty()) {
            getView().showEmptyState();
        } else {
            getView().hideEmptyState();
        }
    }

    public DataLoadState getLoadState() {
        return loadState.getValue();
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }
}
