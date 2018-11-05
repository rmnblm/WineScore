package ch.hsr.winescore.ui.utils;

import android.arch.paging.PagedList;
import ch.hsr.winescore.domain.models.Wine;

public interface ListView<T> extends View {
    void showLoading();
    void hideLoading();
    void showEmptyState();
    void hideEmptyState();
    void showError(String errorMessage);
    void navigateToDetailScreen(android.view.View view, T item);
    void winesUpdated(PagedList<Wine> wines);
}
