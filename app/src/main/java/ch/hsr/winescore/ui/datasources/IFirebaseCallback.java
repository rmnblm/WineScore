package ch.hsr.winescore.ui.datasources;

public interface IFirebaseCallback<T> {
    void onCallback(T result);
}
