package ch.hsr.winescore.domain.utils;

public interface DataLoadStateObserver {
    void onDataLoadStateChanged(DataLoadState loadState);
}
