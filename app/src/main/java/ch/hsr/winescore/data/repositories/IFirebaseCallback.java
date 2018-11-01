package ch.hsr.winescore.data.repositories;

public interface IFirebaseCallback<T> {
    void onCallback(T result);
}
