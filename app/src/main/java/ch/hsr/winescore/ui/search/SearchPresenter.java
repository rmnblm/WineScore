package ch.hsr.winescore.ui.search;

import ch.hsr.winescore.data.prefs.IPreferences;
import ch.hsr.winescore.data.prefs.SharedPreferencesWrapper;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ListView;
import ch.hsr.winescore.ui.wine.WineListPresenter;

public class SearchPresenter extends WineListPresenter<SearchDataSource> {

    private ListView<Wine> view;
    private IPreferences preferences;

    public SearchPresenter() {
        this(new SharedPreferencesWrapper());
    }

    public SearchPresenter(IPreferences preferences) {
        super(new SearchDataSourceFactory(preferences));
        this.preferences = preferences;
    }

    @Override
    public void attachView(ListView<Wine> view) {
        super.attachView(view);
        this.view = view;
    }

    public void setSearchQuery(String query) {
        preferences.putString("pref_search_query", query);
    }

    public void clearPreferences() {
        preferences.clear();
    }

    public void scrollStateChanged(boolean canScrollVertically) {
        if (canScrollVertically) { return; }
        int wineCount = wines.getValue() != null ? wines.getValue().size() : 0;
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
