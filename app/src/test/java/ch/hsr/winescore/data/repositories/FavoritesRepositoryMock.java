package ch.hsr.winescore.data.repositories;

import ch.hsr.winescore.domain.models.Favorite;
import ch.hsr.winescore.domain.models.Wine;

public class FavoritesRepositoryMock implements IFavoritesRepository {

    public Wine favoritedWine;

    @Override
    public void get(Wine wine, ICallback<Favorite> callback) {
        if (favoritedWine != null && favoritedWine.equals(wine)) {
            callback.onCallback(new Favorite());
        } else {
            callback.onCallback(null);
        }
    }

    @Override
    public void set(Wine wine, ICallback<Favorite> callback) {
        favoritedWine = wine;
        callback.onCallback(new Favorite());
    }

    @Override
    public void getCount(ICallback<Integer> callback) {
        callback.onCallback(0);
    }

    @Override
    public void delete(Wine wine, ICallback<Favorite> callback) {
        favoritedWine = null;
        callback.onCallback(null);
    }
}
