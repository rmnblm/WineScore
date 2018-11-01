package ch.hsr.winescore.ui.details;

import ch.hsr.winescore.ui.utils.Presenter;

public class DetailsPresenter implements Presenter<DetailsView> {

    private DetailsView view;

    @Override
    public void attachView(DetailsView view) {
        this.view = view;
    }
}
