package ch.hsr.winescore.ui.latest;

import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ListView;
import ch.hsr.winescore.ui.wine.WineListPresenter;

public class LatestPresenter extends WineListPresenter<LatestDataSource> {

    private ListView<Wine> view;

    public LatestPresenter() {
        super(new LatestDataSourceFactory());
    }

    @Override
    public void attachView(ListView<Wine> view) {
        super.attachView(view);
        this.view = view;
    }

    public void scrollStateChanged(boolean canScrollVertically) {
        if (!canScrollVertically) { view.showLoading(); }
    }

    @Override
    public ListView<Wine> getView() {
        return view;
    }
}
