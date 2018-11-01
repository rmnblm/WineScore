package ch.hsr.winescore.ui.utils;

import ch.hsr.winescore.ui.utils.View;

public interface Presenter<T extends View> {

    void attachView(T view);

}
