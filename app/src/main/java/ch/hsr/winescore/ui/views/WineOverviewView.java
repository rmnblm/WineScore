package ch.hsr.winescore.ui.views;

import ch.hsr.winescore.model.Wine;

import java.util.List;

public interface WineOverviewView extends View {
    void showWineList();
    void showAddedWines(List<Wine> wines);
    void showLoading();
    void hideLoading();
    void showError(String message);
    void navigateToDetailScreen(android.view.View view, int position);
}
