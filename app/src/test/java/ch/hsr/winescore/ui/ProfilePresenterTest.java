package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.net.Uri;
import ch.hsr.winescore.data.repositories.CommentsRepositoryMock;
import ch.hsr.winescore.data.repositories.FavoritesRepositoryMock;
import ch.hsr.winescore.data.repositories.RatingsRepositoryMock;
import ch.hsr.winescore.domain.auth.AuthMock;
import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.domain.auth.UserMock;
import ch.hsr.winescore.ui.profile.ProfilePresenter;
import ch.hsr.winescore.ui.profile.ProfileView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ProfilePresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AuthMock auth;
    private CommentsRepositoryMock commentsRepo;
    private FavoritesRepositoryMock favoritesRepo;
    private RatingsRepositoryMock ratingsRepo;
    private ProfileViewMock view;
    private ProfilePresenter presenter;

    @Before
    public void setUp() throws IOException {
        auth = new AuthMock();
        commentsRepo = new CommentsRepositoryMock();
        favoritesRepo = new FavoritesRepositoryMock();
        ratingsRepo = new RatingsRepositoryMock();
        view = new ProfileViewMock();
        presenter = new ProfilePresenter(auth, commentsRepo, favoritesRepo, ratingsRepo);
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

    @Test
    public void whenLoadCountsIsCalled_itRefreshesCommentsFavoritesRatingsCount() {
        presenter.loadCounts();
        assertTrue(view.refreshCommentsCountCalled);
        assertTrue(view.refreshFavoritesCountCalled);
        assertTrue(view.refreshRatingsCountCalled);
    }
}

