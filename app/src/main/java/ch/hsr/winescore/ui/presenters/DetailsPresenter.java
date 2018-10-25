package ch.hsr.winescore.ui.presenters;

import ch.hsr.winescore.ui.views.DetailsView;

public class DetailsPresenter implements Presenter<DetailsView> {

    private DetailsView view;

    @Override
    public void attachView(DetailsView view) {
        this.view = view;
    }
}
