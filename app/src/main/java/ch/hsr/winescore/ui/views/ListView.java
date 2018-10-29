package ch.hsr.winescore.ui.views;

import ch.hsr.winescore.model.Wine;

public interface ListView<T> extends View {
    void showLoading();
    void hideLoading();
    void showEmptyState();
    void hideEmptyState();
    void showError();
    void navigateToDetailScreen(android.view.View view, T item);
}
