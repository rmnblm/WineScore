package ch.hsr.winescore.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import ch.hsr.winescore.domain.models.Favorite;
import ch.hsr.winescore.domain.models.Wine;

public class FavoritesFirebaseRepository extends FirebaseRepository implements IFavoritesRepository {

    private static final String COLLECTION = "favorites";

    public void set(Wine wine, ICallback<Favorite> callback) {
        WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    Favorite favorite = new Favorite(FirebaseAuth.getInstance().getUid(), wine.getId());
                    getFavoriteReference(wine).set(favorite, SetOptions.merge())
                            .addOnSuccessListener(aVoid1 -> callback.onCallback(favorite))
                            .addOnFailureListener(e -> callback.onCallback(null));
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }


    public void get(Wine wine, ICallback<Favorite> callback) {
        getFavoriteReference(wine).get()
                .addOnSuccessListener(result -> callback.onCallback(result.toObject(Favorite.class)))
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public void delete(Wine wine, ICallback<Favorite> callback) {
        getFavoriteReference(wine).delete()
                .addOnSuccessListener(result -> callback.onCallback(null))
                .addOnFailureListener(result -> callback.onCallback(new Favorite()));
    }

    public void getCount(ICallback<Integer> callback) {
        countCollectionItemsByUser(COLLECTION, callback);
    }

    private static DocumentReference getFavoriteReference(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION).document(wine.getId() + "-" + FirebaseAuth.getInstance().getUid());
    }
}
