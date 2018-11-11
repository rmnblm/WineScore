package ch.hsr.winescore.ui.latest;

import ch.hsr.winescore.data.prefs.IPreferences;
import ch.hsr.winescore.data.prefs.SharedPreferencesWrapper;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ListView;
import ch.hsr.winescore.ui.wine.WineListPresenter;

public class LatestPresenter extends WineListPresenter<LatestDataSource> {

    private ListView<Wine> view;
    private IPreferences preferences;

    public LatestPresenter() {
        this(new SharedPreferencesWrapper());
    }

    public LatestPresenter(IPreferences preferences) {
        super(new LatestDataSourceFactory(preferences));
        this.preferences = preferences;
    }

    @Override
    public void attachView(ListView<Wine> view) {
        super.attachView(view);
        this.view = view;
    }

    public void clearPreferences() {
        preferences.clear();
    }

    public void scrollStateChanged(boolean canScrollVertically) {
        if (!canScrollVertically) { view.showLoading(); }
    }

    @Override
    protected ListView<Wine> getView() {
        return view;
    }
}
