package ch.hsr.winescore.utils;

import ch.hsr.winescore.model.DataLoadState;

public interface DataLoadStateObserver {
    void onDataLoadStateChanged(DataLoadState loadState);
}
