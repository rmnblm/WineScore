package ch.hsr.winescore.ui.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.SharedPreferences;
import android.view.View;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.SearchDataSourceFactory;
import ch.hsr.winescore.ui.views.SearchView;

public class SearchPresenter implements Presenter<SearchView> {

    private SearchView view;
    private static final int PAGE_SIZE = 50;
    private LiveData<PagedList<Wine>> wines;
    private final SearchDataSourceFactory dataSourceFactory;
    private final MutableLiveData<DataLoadState> loadState;
    private final SharedPreferences sharedPreferences;

    public SearchPresenter() {
        this.dataSourceFactory = new SearchDataSourceFactory();
        this.loadState = new MutableLiveData<>();
        this.sharedPreferences = WineScoreApplication.getSharedPreferences();
    }

    @Override
    public void attachView(SearchView view) {
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

    public void setSearchQuery(String query) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_search_query",query);
        editor.apply();
    }
}
