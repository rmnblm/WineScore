package ch.hsr.winescore.ui;

import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.ListView;

public class LatestViewMock implements ListView<Wine> {

    boolean showLoadingCalled = false;
    boolean hideLoadingCalled = false;
    boolean showErrorCalled = false;
    boolean navigateToDetailScreenCalled = false;
    Wine wine = null;

    boolean winesUpdatedCalled = false;

    @Override
    public void showLoading() {
        showLoadingCalled = true;
    }

    @Override
    public void hideLoading() {
        hideLoadingCalled = true;
    }

    @Override
    public void showEmptyState() {

    }

    @Override
    public void hideEmptyState() {

    }

    @Override
    public void showError(String message) {
        showErrorCalled = true;
    }

    @Override
    public void navigateToDetailScreen(View view, Wine wine) {
        navigateToDetailScreenCalled = true;
        this.wine = wine;
    }

    @Override
    public void winesUpdated(PagedList<Wine> wines) {
        winesUpdatedCalled = true;
    }
}
