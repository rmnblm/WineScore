package ch.hsr.winescore.ui.views;

public interface ListView<T> extends View {
    void showLoading();
    void hideLoading();
    void showEmptyState();
    void hideEmptyState();
    void showError();
    void navigateToDetailScreen(android.view.View view, T item);
}
