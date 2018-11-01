package ch.hsr.winescore.ui.search;

import android.content.SharedPreferences;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.ui.utils.Presenter;
import ch.hsr.winescore.ui.wine.WineListPresenter;
import ch.hsr.winescore.ui.wine.WineListView;

public class SearchPresenter extends WineListPresenter<SearchDataSource> implements Presenter<SearchView> {

    private SearchView view;
    private final SharedPreferences sharedPreferences;

    public SearchPresenter() {
        super(new SearchDataSourceFactory());
        this.sharedPreferences = WineScoreApplication.getSharedPreferences();
    }

    @Override
    public void attachView(SearchView view) {
        super.attachView(view);
        this.view = view;
    }

    public void setSearchQuery(String query) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_search_query",query);
        editor.apply();
    }

    public void scrollStateChanged(boolean canScrollVertically) {
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
