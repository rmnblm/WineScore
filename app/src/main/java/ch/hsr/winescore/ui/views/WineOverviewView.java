package ch.hsr.winescore.ui.views;

import ch.hsr.winescore.model.Wine;

public interface WineOverviewView extends View {
    void showInitialLoading();
    void hideInitialLoading();
    void showError(String errorMessage);
    void navigateToDetailScreen(android.view.View view, Wine wine);
}
