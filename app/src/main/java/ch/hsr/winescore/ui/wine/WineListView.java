package ch.hsr.winescore.ui.wine;

import android.arch.lifecycle.LifecycleOwner;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.View;

public interface WineListView extends View {
    void showLoading();
    void hideLoading();
    void showError(String errorMessage);
    void navigateToDetailScreen(android.view.View view, Wine wine);
}
