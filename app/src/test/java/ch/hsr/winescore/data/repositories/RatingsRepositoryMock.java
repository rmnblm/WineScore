package ch.hsr.winescore.data.repositories;

import android.util.SparseIntArray;
import ch.hsr.winescore.domain.models.Rating;
import ch.hsr.winescore.domain.models.Wine;

public class RatingsRepositoryMock implements IRatingsRepository {

    public Wine ratedWine;
    public Rating rating;

    public Wine unratedWine;

    @Override
    public void get(Wine wine, ICallback<Rating> callback) {
        if (ratedWine != null && ratedWine.equals(wine)) {
            callback.onCallback(rating);
        } else {
            callback.onCallback(null);
        }
    }

    @Override
    public void set(Wine wine, int ratingValue, ICallback<Rating> callback) {
        if (unratedWine != null && unratedWine.equals(wine)) {
            callback.onCallback(new Rating("userId", wine.getId(), ratingValue));
        } else {
            callback.onCallback(null);
        }
    }

    @Override
    public void getCount(ICallback<Integer> callback) {
        callback.onCallback(0);
    }

    @Override
    public void delete(Wine wine, ICallback<Rating> callback) {
        ratedWine = null;
        callback.onCallback(null);
    }

    @Override
    public void getRatings(Wine wine, ICallback<SparseIntArray> callback) {
        callback.onCallback(new SparseIntArray());
    }
}
