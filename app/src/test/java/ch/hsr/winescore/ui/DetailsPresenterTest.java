package ch.hsr.winescore.ui;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.net.Uri;
import ch.hsr.winescore.data.repositories.CommentsRepositoryMock;
import ch.hsr.winescore.data.repositories.FavoritesRepositoryMock;
import ch.hsr.winescore.data.repositories.RatingsRepositoryMock;
import ch.hsr.winescore.domain.auth.AuthMock;
import ch.hsr.winescore.domain.auth.IUser;
import ch.hsr.winescore.domain.auth.UserMock;
import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Rating;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.details.DetailsPresenter;
import ch.hsr.winescore.ui.profile.ProfilePresenter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DetailsPresenterTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private CommentsRepositoryMock commentsRepo;
    private FavoritesRepositoryMock favoritesRepo;
    private RatingsRepositoryMock ratingsRepo;
    private DetailsViewMock view;
    private DetailsPresenter presenter;

    @Before
    public void setUp() {
        commentsRepo = new CommentsRepositoryMock();
        favoritesRepo = new FavoritesRepositoryMock();
        ratingsRepo = new RatingsRepositoryMock();
        view = new DetailsViewMock();
        presenter = new DetailsPresenter(commentsRepo, favoritesRepo, ratingsRepo);
        presenter.attachView(view);
    }

    @Test
    public void whenFavoritedWine_itCallsRefreshFavoriteButtonWithTrue() {
        Wine wine = new Wine("favorited", "1");
        favoritesRepo.favoritedWine = wine;
        presenter.isFavorite(wine);
        assertTrue(view.refreshFavoriteButtonValue);
    }

    @Test
    public void whenNotFavoritedWine_itCallsRefreshFavoriteButtonWithFalse() {
        Wine favorited = new Wine("favorited", "1");
        Wine not_favorited = new Wine("not favorited", "2");
        favoritesRepo.favoritedWine = favorited;
        presenter.isFavorite(not_favorited);
        assertFalse(view.refreshFavoriteButtonValue);
    }

    @Test
    public void whenAddingWineAsFavourite_itCallsOnFavouriteAdded() {
        Wine newFavorite = new Wine("newFavourite", "1");
        presenter.addAsFavorite(newFavorite);
        assertEquals(newFavorite, favoritesRepo.favoritedWine);
        assertTrue(view.onFavoriteAddedCalled);
    }

    @Test
    public void whenRemovingWineAsFavourite_itShouldDeleteItFromRepoAndCallsOnFavouriteRemoved() {
        Wine favorited = new Wine("favorited", "1");
        favoritesRepo.favoritedWine = favorited;
        presenter.removeAsFavorite(favorited);
        assertTrue(view.onFavoriteRemovedCalled);
        assertNull(favoritesRepo.favoritedWine);
    }

    @Test
    public void whenLoadingRatings_itRefreshesRatingsOnView() {
        Wine wine = new Wine("wine", "1");
        presenter.loadRatings(wine);
        assertTrue(view.refreshRatingsCalled);
    }

    @Test
    public void whenLoadingRatedWine_itShowsMyRatingInView() {
        Wine wine = new Wine("wine", "1");
        ratingsRepo.ratedWine = wine;
        ratingsRepo.rating = new Rating("userId", wine.getId(), 3);

        presenter.loadMyRating(wine);
        assertTrue(view.showMyRatingCalled);
        assertEquals(3, view.showMyRatingValue);
    }

    @Test
    public void whenLoadingUnratedWine_itHidesMyRatingInView() {
        Wine rated = new Wine("wine", "1");
        Wine not_rated = new Wine("other wine", "2");
        ratingsRepo.ratedWine = rated;
        presenter.loadMyRating(not_rated);
        assertTrue(view.hideMyRatingCalled);
    }

    @Test
    public void whenRatingAWine_itShouldReloadTheRatingsAndShowMyRatingInView() {
        Wine not_rated = new Wine("other wine", "2");
        ratingsRepo.unratedWine = not_rated;
        presenter.setMyRating(not_rated, 3);
        assertTrue(view.refreshRatingsCalled);
        assertTrue(view.showMyRatingCalled);
        assertEquals(3, view.showMyRatingValue);
    }

    @Test
    public void whenRatingCausesAFailure_itShouldHideMyRatingInView() {
        presenter.setMyRating(null, 3);
        assertTrue(view.hideMyRatingCalled);
    }

    @Test
    public void whenRemovingARating_itShouldDeleteItFromRepoAndRefreshRatingsAndHideMyRatingInView() {
        Wine rated = new Wine("wine", "1");
        ratingsRepo.ratedWine = rated;
        presenter.removeMyRating(rated);
        assertTrue(view.refreshRatingsCalled);
        assertTrue(view.hideMyRatingCalled);
        assertNull(ratingsRepo.ratedWine);
    }

    @Test
    public void whenLoadingLastComment_itShouldGetTheLastCommentFromRepoAndRefreshInView() {
        Wine commented = new Wine("wine", "1");
        Comment comment = new Comment("userId", "userName", commented.getId(), "My Comment");
        commentsRepo.commented = commented;
        commentsRepo.comment = comment;

        presenter.loadLastComment(commented);
        assertTrue(view.refreshLastCommentCalled);
        assertEquals(comment, view.refreshLastCommentValue);
    }
}

