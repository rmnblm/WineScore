package ch.hsr.winescore.model;

public interface DataLoadStateObserver {
    void onDataLoadStateChanged(DataLoadState loadState);
}
