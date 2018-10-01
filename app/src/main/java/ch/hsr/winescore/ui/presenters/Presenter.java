package ch.hsr.winescore.ui.presenters;

import ch.hsr.winescore.ui.views.View;

public interface Presenter<T extends View> {

    void attachView(T view);

}
