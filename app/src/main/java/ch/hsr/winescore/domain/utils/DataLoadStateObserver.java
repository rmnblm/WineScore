package ch.hsr.winescore.domain.utils;

import ch.hsr.winescore.domain.utils.DataLoadState;

public interface DataLoadStateObserver {
    void onDataLoadStateChanged(DataLoadState loadState);
}
