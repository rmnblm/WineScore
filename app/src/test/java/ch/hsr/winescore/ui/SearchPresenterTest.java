package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.paging.PagedList;
import android.view.View;
import ch.hsr.winescore.WineScoreConstants;
import ch.hsr.winescore.data.prefs.PreferencesMock;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.search.SearchPresenter;
import ch.hsr.winescore.ui.utils.ListView;
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

    private PreferencesMock preferences;
    private SearchViewMock view;
    private SearchPresenter presenter;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        WineScoreConstants.setRootUrl(server.url("/").toString());

        preferences = new PreferencesMock();
        view = new SearchViewMock();
        presenter = new SearchPresenter(preferences);
        presenter.attachView(view);
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
}

