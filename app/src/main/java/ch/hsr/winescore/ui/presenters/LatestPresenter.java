package ch.hsr.winescore.ui.presenters;

import ch.hsr.winescore.ui.datasources.LatestDataSource;
import ch.hsr.winescore.ui.datasources.LatestDataSourceFactory;
import ch.hsr.winescore.ui.views.LatestView;
import ch.hsr.winescore.ui.views.WineListView;

public class LatestPresenter extends WineListPresenter<LatestDataSource> implements Presenter<LatestView> {

    private LatestView view;

    public LatestPresenter() {
        super(new LatestDataSourceFactory());
    }

    @Override
    public void attachView(LatestView view) {
        this.view = view;
        setupLiveWineData(dataSourceFactory);
        setupLoadStateObserver(dataSourceFactory);
    }

    public void refreshData() {
        dataSourceFactory.invalidateDataSource();
    }

    public void reachedEndOfList(boolean canScrollVertically) {
        if (!canScrollVertically) { view.showLoading(); }
    }

    @Override
    protected WineListView getView() {
        return view;
    }
}
