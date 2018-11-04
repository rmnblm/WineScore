package ch.hsr.winescore.ui.details;

import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;
import ch.hsr.winescore.data.repositories.FavoritesFirebaseRepository;
import ch.hsr.winescore.data.repositories.RatingsFirebaseRepository;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.Presenter;

public class DetailsPresenter implements Presenter<DetailsView> {

    private DetailsView view;

    @Override
    public void attachView(DetailsView view) {
        this.view = view;
    }

    public void isFavorite(Wine wine) {
        FavoritesFirebaseRepository.get(wine, result -> view.refreshFavoriteButton(result != null));
    }

    public void addAsFavorite(Wine wine) {
        FavoritesFirebaseRepository.set(wine, result -> {
            if (result != null) {
                view.onFavoriteAdded();
            }
        });
    }

    public void removeAsFavorite(Wine wine) {
        FavoritesFirebaseRepository.delete(wine, result -> {
            if (result == null) {
                view.onFavoriteRemoved();
            }
        });
    }

    public void loadRatings(Wine wine) {
        RatingsFirebaseRepository.getRatings(wine, result -> view.refreshRatings(result));
    }

    public void loadMyRating(Wine wine) {
        RatingsFirebaseRepository.get(wine, result -> {
            if (result != null) {
                view.showMyRating(result.getRatingValue());
            } else {
                view.hideMyRating();
            }
        });
    }

    public void setMyRating(Wine wine, int rating) {
        RatingsFirebaseRepository.set(wine, rating, result -> {
            if (result != null) {
                loadRatings(wine);
                view.showMyRating(result.getRatingValue());
            } else {
                view.hideMyRating();
            }
        });
    }

    public void removeMyRating(Wine wine) {
        RatingsFirebaseRepository.delete(wine, result -> {
            if (result == null) {
                loadRatings(wine);
                view.hideMyRating();
            }
        });
    }

    public void loadLastComment(Wine wine) {
        CommentsFirebaseRepository.getLast(wine, comment -> {
            if (comment != null) {
                view.refreshLastComment(comment);
            }
        });
    }
}
