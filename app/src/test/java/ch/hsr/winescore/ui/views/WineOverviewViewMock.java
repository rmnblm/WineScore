package ch.hsr.winescore.ui.views;

import android.view.View;
import ch.hsr.winescore.model.Wine;

import java.util.List;

public class WineOverviewViewMock implements WineOverviewView {

    public boolean showWineListCalled = false;

    @Override
    public void showWineList() {
        showWineListCalled = true;
    }

    @Override
    public void showAddedWines(List<Wine> wines) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void navigateToDetailScreen(View view, int position) {

    }
}
