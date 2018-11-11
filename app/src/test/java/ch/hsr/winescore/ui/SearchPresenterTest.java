package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.view.View;
import ch.hsr.winescore.WineScoreConstants;
import ch.hsr.winescore.data.prefs.MockPreferences;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.domain.utils.DataLoadState;
import ch.hsr.winescore.ui.latest.LatestPresenter;
import ch.hsr.winescore.ui.search.SearchPresenter;
import ch.hsr.winescore.ui.utils.ListView;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SearchPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MockPreferences preferences;
    private SearchViewMock view;
    private SearchPresenter presenter;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        WineScoreConstants.setRootUrl(server.url("/").toString());

        preferences = new MockPreferences();
        view = new SearchViewMock();
        presenter = new SearchPresenter(preferences);
        presenter.attachView(view);
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
    public void whenSettingSearchQuery_itAddsItToPreferences() {
        presenter.setSearchQuery("test");
        assertTrue(preferences.map.containsKey("pref_search_query"));
        assertEquals("test", preferences.map.get("pref_search_query"));
    }

    @Test
    public void whenClearingPreferences_itClearsPreferences() {
        preferences.map.put("foo", "bar");
        assertTrue(preferences.map.size() == 1);
        presenter.clearPreferences();
        assertTrue(preferences.map.size() == 0);
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }

    private class SearchViewMock implements ListView<Wine> {

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
}

