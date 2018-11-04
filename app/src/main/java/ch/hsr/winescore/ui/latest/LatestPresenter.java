package ch.hsr.winescore.ui.latest;

import ch.hsr.winescore.ui.utils.Presenter;
import ch.hsr.winescore.ui.wine.WineListPresenter;
import ch.hsr.winescore.ui.wine.WineListView;

public class LatestPresenter extends WineListPresenter<LatestDataSource> implements Presenter<LatestView> {

    private LatestView view;

    public LatestPresenter() {
        super(new LatestDataSourceFactory());
    }

    @Override
    public void attachView(LatestView view) {
        super.attachView(view);
        this.view = view;
    }

    public void scrollStateChanged(boolean canScrollVertically) {
        if (!canScrollVertically) { view.showLoading(); }
    }

    @Override
    public WineListView getView() {
        return view;
    }
}
