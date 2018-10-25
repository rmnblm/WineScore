package ch.hsr.winescore.ui.presenters;

import android.view.View;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.views.LatestView;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class LatestPresenterTest {

    private LatestViewMock view;
    private LatestPresenter presenter;

    @Before
    public void setupPresenter() {
        view = new LatestViewMock();
        presenter = new LatestPresenter();
        presenter.attachView(view);
    }

    @Test
    public void whenSubscribingToPresenter_itShowsTheWineList() {
        presenter.subscribe();
        assertTrue(view.showWineListCalled);
    }

    public class LatestViewMock implements LatestView {

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
}
