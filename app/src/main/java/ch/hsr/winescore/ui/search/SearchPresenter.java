package ch.hsr.winescore.ui.search;

import android.content.SharedPreferences;

import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ListView;
import ch.hsr.winescore.ui.wine.WineListPresenter;

public class SearchPresenter extends WineListPresenter<SearchDataSource> {

    private ListView<Wine> view;
    private final SharedPreferences sharedPreferences;

    public SearchPresenter() {
        super(new SearchDataSourceFactory());
        this.sharedPreferences = WineScoreApplication.getSharedPreferences();
    }

    public void attachView(ListView<Wine> view) {
        super.attachView(view);
        this.view = view;
    }

    public void setSearchQuery(String query) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_search_query",query);
        editor.apply();
    }

    public void clearPreferences() {
        sharedPreferences.edit().clear().apply();
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
    protected ListView<Wine> getView() {
        return view;
    }
}
