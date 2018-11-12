package ch.hsr.winescore.data.repositories;

public interface ICallback<T> {
    void onCallback(T result);
}
