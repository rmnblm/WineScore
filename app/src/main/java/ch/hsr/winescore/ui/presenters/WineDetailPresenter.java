package ch.hsr.winescore.ui.presenters;

import ch.hsr.winescore.ui.views.WineDetailView;

public class WineDetailPresenter implements Presenter<WineDetailView> {

    private WineDetailView view;

    @Override
    public void attachView(WineDetailView view) {
        this.view = view;
    }
}
