package ch.hsr.winescore.ui.details;

import ch.hsr.winescore.data.repositories.*;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.Presenter;

public class DetailsPresenter implements Presenter<DetailsView> {

    private DetailsView view;
    private ICommentsRepository commentsRepo;
    private IFavoritesRepository favoritesRepo;
    private IRatingsRepository ratingsRepo;

    public DetailsPresenter() {
        this(new CommentsFirebaseRepository(), new FavoritesFirebaseRepository(), new RatingsFirebaseRepository());
    }

    public DetailsPresenter(
            ICommentsRepository commentsRepo,
            IFavoritesRepository favoritesRepo,
            IRatingsRepository ratingsRepo) {
        this.commentsRepo = commentsRepo;
        this.favoritesRepo = favoritesRepo;
        this.ratingsRepo = ratingsRepo;
    }

    @Override
    public void attachView(DetailsView view) {
        this.view = view;
    }

    public void isFavorite(Wine wine) {
        favoritesRepo.get(wine, result -> view.refreshFavoriteButton(result != null));
    }

    public void addAsFavorite(Wine wine) {
        favoritesRepo.set(wine, result -> {
            if (result != null) {
                view.onFavoriteAdded();
            }
        });
    }

    public void removeAsFavorite(Wine wine) {
        favoritesRepo.delete(wine, result -> {
            if (result == null) {
                view.onFavoriteRemoved();
            }
        });
    }

    public void loadRatings(Wine wine) {
        ratingsRepo.getRatings(wine, result -> view.refreshRatings(result));
    }

    public void loadMyRating(Wine wine) {
        ratingsRepo.get(wine, result -> {
            if (result != null) {
                view.showMyRating(result.getRatingValue());
            } else {
                view.hideMyRating();
            }
        });
    }

    public void setMyRating(Wine wine, int rating) {
        ratingsRepo.set(wine, rating, result -> {
            if (result != null) {
                loadRatings(wine);
                view.showMyRating(result.getRatingValue());
            } else {
                view.hideMyRating();
            }
        });
    }

    public void removeMyRating(Wine wine) {
        ratingsRepo.delete(wine, result -> {
            if (result == null) {
                loadRatings(wine);
                view.hideMyRating();
            }
        });
    }

    public void loadLastComment(Wine wine) {
        commentsRepo.getLast(wine, comment -> {
            if (comment != null) {
                view.refreshLastComment(comment);
            }
        });
    }
}
