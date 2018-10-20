package ch.hsr.winescore.ui.views;

import ch.hsr.winescore.model.Wine;

public interface ListView extends View {
    void showLoading();
    void hideLoading();
    void showError();
    void navigateToDetailScreen(android.view.View view, Wine wine);
}
