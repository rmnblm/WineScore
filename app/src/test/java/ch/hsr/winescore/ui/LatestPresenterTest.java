package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.view.View;
import ch.hsr.winescore.WineScoreConstants;
import ch.hsr.winescore.data.prefs.PreferencesMock;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.domain.utils.DataLoadState;
import ch.hsr.winescore.ui.latest.LatestPresenter;
import ch.hsr.winescore.ui.utils.ListView;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class LatestPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private PreferencesMock preferences;
    private LatestViewMock view;
    private LatestPresenter presenter;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        WineScoreConstants.setRootUrl(server.url("/").toString());

        preferences = new PreferencesMock();
        view = new LatestViewMock();
        presenter = new LatestPresenter(preferences);
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
    public void whenListItemClicked_itNavigatesToDetailScreen() throws InterruptedException {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setHeader("content-type", "application/json");
        response.setBody("{\n" +
                "           \"count\": 1,\n" +
                "           \"results\": [\n" +
                "           {\n" +
                "            \"wine\": \"Terrazas De Los Andes, Reserva Malbec, Mendoza\",\n" +
                "            \"wine_id\": 133251,\n" +
                "            \"wine_slug\": \"terrazas-de-los-andes-reserva-malbec-mendoza\",\n" +
                "            \"appellation\": \"Mendoza\",\n" +
                "            \"appellation_slug\": \"mendoza\",\n" +
                "            \"color\": \"Red\",\n" +
                "            \"wine_type\": \"\",\n" +
                "            \"regions\": [\n" +
                "                \"Mendoza\"\n" +
                "            ],\n" +
                "            \"country\": \"Argentina\",\n" +
                "            \"classification\": null,\n" +
                "            \"vintage\": \"2015\",\n" +
                "            \"date\": \"2018-12-01\",\n" +
                "            \"is_primeurs\": false,\n" +
                "            \"score\": 88.59,\n" +
                "            \"confidence_index\": \"B+\",\n" +
                "            \"journalist_count\": 4,\n" +
                "            \"lwin\": null,\n" +
                "            \"lwin_11\": null\n" +
                "           }" +
                "       ]" +
                "}");
        server.enqueue(response);

        LifecycleOwnerMock owner = new LifecycleOwnerMock();
        owner.registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

        presenter.bindLoadState(owner);
        presenter.bindWines(owner); // automatically executes network call to load wines

        while (presenter.getLoadState() != DataLoadState.LOADED);

        presenter.listItemClicked(null, 0);
        assertTrue(view.navigateToDetailScreenCalled);
        assertTrue(view.winesUpdatedCalled);
        assertEquals("Terrazas De Los Andes, Reserva Malbec, Mendoza", view.wine.getName());
    }

    @After
    public void tearDown() throws IOException {
        server.shutdown();
    }
}

