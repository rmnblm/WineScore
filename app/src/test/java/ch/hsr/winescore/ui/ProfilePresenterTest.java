package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.paging.PagedList;
import android.net.Uri;
import android.view.View;
import ch.hsr.winescore.WineScoreConstants;
import ch.hsr.winescore.data.prefs.MockPreferences;
import ch.hsr.winescore.domain.auth.AuthMock;
import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.domain.auth.UserMock;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.profile.ProfilePresenter;
import ch.hsr.winescore.ui.profile.ProfileView;
import ch.hsr.winescore.ui.search.SearchPresenter;
import ch.hsr.winescore.ui.utils.ListView;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.*;

public class ProfilePresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AuthMock auth;
    private ProfileViewMock view;
    private ProfilePresenter presenter;

    @Before
    public void setUp() throws IOException {
        auth = new AuthMock();
        view = new ProfileViewMock();
        presenter = new ProfilePresenter(auth);
        presenter.attachView(view);
    }

    @Test
    public void whenAuthReturnsCurrentUser_itCallsOnSignedIn() {
        IUser user = new UserMock("Batman", Uri.EMPTY, true);
        auth.currentUser = user;
        presenter.checkAuthentication();
        assertTrue(view.onSignedInCalled);
        assertFalse(view.onSignedOutCalled);
        assertEquals(user, view.onSignInUser);
    }

    @Test
    public void whenAuthDoesNotReturnCurrentUser_itCallsOnSignedOut() {
        presenter.checkAuthentication();
        assertFalse(view.onSignedInCalled);
        assertTrue(view.onSignedOutCalled);
        assertNull(view.onSignInUser);
    }

    @Test
    public void whenSignOutIsCalled_itSignsOutFromAuth() {
        presenter.signOut();
        assertTrue(view.onSignedOutCalled);
        assertTrue(auth.signOutCalled);
    }

    private class ProfileViewMock implements ProfileView {

        public boolean onSignedInCalled = false;
        public IUser onSignInUser;
        public boolean onSignedOutCalled = false;

        @Override
        public void onSignedIn(IUser user) {
            onSignedInCalled = true;
            onSignInUser = user;
        }

        @Override
        public void onSignedOut() {
            onSignedOutCalled = true;
        }

        @Override
        public void refreshFavoritesCount(int count) {

        }

        @Override
        public void refreshRatingsCount(int count) {

        }

        @Override
        public void refreshCommentsCount(int count) {

        }
    }
}

