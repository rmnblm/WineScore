package ch.hsr.winescore.ui.presenters;

import android.content.SharedPreferences;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.ui.datasources.SearchDataSource;
import ch.hsr.winescore.ui.datasources.SearchDataSourceFactory;
import ch.hsr.winescore.ui.views.WineListView;
import ch.hsr.winescore.ui.views.SearchView;

public class SearchPresenter extends WineListPresenter<SearchDataSource> implements Presenter<SearchView> {

    private SearchView view;
    private final SharedPreferences sharedPreferences;
    private boolean isObservingWines = false;

    public SearchPresenter() {
        super(new SearchDataSourceFactory());
        this.sharedPreferences = WineScoreApplication.getSharedPreferences();
    }

    @Override
    public void attachView(SearchView view) {
        this.view = view;
        setupLiveWineData(dataSourceFactory);
        setupLoadStateObserver(dataSourceFactory);
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }

    public void setSearchQuery(String query) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_search_query",query);
        editor.apply();
    }

    public void reachedEndOfList(boolean canScrollVertically) {
        if (canScrollVertically) { return; }
        int wineCount = wines.getValue().size();
        int totalWineCount = dataSourceFactory.getTotalCountOfCurrentDataSource();
        if (wineCount < totalWineCount) {
            view.showLoading();
        }
    }

    @Override
    protected WineListView getView() {
        return view;
    }
}
