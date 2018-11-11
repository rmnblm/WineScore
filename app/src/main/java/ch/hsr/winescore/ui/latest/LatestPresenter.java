package ch.hsr.winescore.ui.latest;

import android.content.SharedPreferences;

import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ListView;
import ch.hsr.winescore.ui.wine.WineListPresenter;

public class LatestPresenter extends WineListPresenter<LatestDataSource> {

    private ListView<Wine> view;
    private final SharedPreferences sharedPreferences;

    public LatestPresenter() {
        super(new LatestDataSourceFactory());
        this.sharedPreferences = WineScoreApplication.getSharedPreferences();
    }

    @Override
    public void attachView(ListView<Wine> view) {
        super.attachView(view);
        this.view = view;
    }

    public void clearPreferences() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }

    public void scrollStateChanged(boolean canScrollVertically) {
        if (!canScrollVertically) { view.showLoading(); }
    }

    @Override
    public ListView<Wine> getView() {
        return view;
    }
}
