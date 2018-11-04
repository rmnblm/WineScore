package ch.hsr.winescore.ui.latest;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;
import android.view.View;
import ch.hsr.winescore.domain.models.Wine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LatestPresenterTest {

    private LatestViewMock view;
    private LatestPresenter presenter;

    @Before
    public void setupPresenter() {
        view = new LatestViewMock();
        presenter = new LatestPresenter();
        presenter.attachView(view);
        presenter.bindLoadState(TEST_LIFECYCLE_OWNER);
        presenter.bindWines(TEST_LIFECYCLE_OWNER);
    }

    @Test
    public void whenReachingEndOfList_itCallsShowLoading() {
        presenter.scrollStateChanged(false);
        assertTrue(view.showLoadingCalled);
    }

    @Test
    public void whenNotReachingEndOfList_itDoesNotCallShowLoading() {
        presenter.scrollStateChanged(true);
        assertFalse(view.showLoadingCalled);
    }

    @Test
    public void whenAttachingView_itEnsuresThatViewIsNotNull() {
        assertNotNull(presenter.getView());
    }

    @Test
    public void whenListItemClicked_itNavigatesToDetailScreen() {
        presenter.listItemClicked(null, 0);
        assertTrue(view.navigateToDetailScreenCalled);
    }

    private class LatestViewMock implements LatestView {

        boolean showLoadingCalled = false;
        boolean hideLoadingCalled = false;
        boolean showErrorCalled = false;
        boolean navigateToDetailScreenCalled = false;
        boolean showEmptyStateCalled = false;
        boolean hideEmptyStateCalled = false;

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
            showEmptyStateCalled = true;
        }

        @Override
        public void hideEmptyState() {
            hideEmptyStateCalled = true;
        }

        @Override
        public void showError(String message) {
            showErrorCalled = true;
        }

        @Override
        public void navigateToDetailScreen(View view, Wine wine) {
            navigateToDetailScreenCalled = true;
        }
    }

    private static LifecycleOwner TEST_LIFECYCLE_OWNER = new LifecycleOwner() {

        Lifecycle lifecycle = new Lifecycle() {
            @Override
            public void addObserver(@NonNull LifecycleObserver observer) {

            }

            @Override
            public void removeObserver(@NonNull LifecycleObserver observer) {

            }

            @NonNull
            @Override
            public State getCurrentState() {
                return State.STARTED;
            }
        };

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return lifecycle;
        }
    };

}

