package ch.hsr.winescore.data.repositories;

import ch.hsr.winescore.domain.models.Favorite;
import ch.hsr.winescore.domain.models.Wine;

public interface IFavoritesRepository {
    void get(Wine wine, ICallback<Favorite> callback);
    void set(Wine wine, ICallback<Favorite> callback);
    void getCount(ICallback<Integer> callback);
    void delete(Wine wine, ICallback<Favorite> callback);
}
