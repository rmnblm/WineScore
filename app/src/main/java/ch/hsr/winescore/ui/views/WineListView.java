package ch.hsr.winescore.ui.views;

import ch.hsr.winescore.model.Wine;

public interface WineListView extends View {
    void showLoading();
    void hideLoading();
    void showError(String errorMessage);
    void navigateToDetailScreen(android.view.View view, Wine wine);
}
