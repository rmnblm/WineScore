package ch.hsr.winescore.data.repositories;

import android.util.SparseIntArray;
import ch.hsr.winescore.domain.models.Rating;
import ch.hsr.winescore.domain.models.Wine;

public interface IRatingsRepository {
    void get(Wine wine, ICallback<Rating> callback);
    void set(Wine wine, int ratingValue, ICallback<Rating> callback);
    void getCount(ICallback<Integer> callback);
    void delete(Wine wine, ICallback<Rating> callback);
    void getRatings(Wine wine, ICallback<SparseIntArray> callback);
}
