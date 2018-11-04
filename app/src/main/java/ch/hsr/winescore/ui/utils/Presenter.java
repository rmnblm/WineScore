package ch.hsr.winescore.ui.utils;

public interface Presenter<T extends View> {
    void attachView(T view);
}
