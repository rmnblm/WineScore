package ch.hsr.winescore.ui.utils;

public interface ListView<T> extends View {
    void showLoading();
    void hideLoading();
    void showEmptyState();
    void hideEmptyState();
    void showError(String errorMessage);
    void navigateToDetailScreen(android.view.View view, T item);
}
