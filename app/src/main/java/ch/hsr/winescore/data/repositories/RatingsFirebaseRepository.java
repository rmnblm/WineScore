package ch.hsr.winescore.data.repositories;

import android.util.SparseIntArray;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import ch.hsr.winescore.domain.models.Rating;
import ch.hsr.winescore.domain.models.Wine;

public class RatingsFirebaseRepository extends FirebaseRepository {

    private static final String COLLECTION = "ratings";
    private static final String FIELD_WINE_ID = "wineId";

    public static void get(Wine wine, IFirebaseCallback<Rating> callback) {
        getRatingReference(wine).get()
                .addOnSuccessListener(result -> callback.onCallback(result.toObject(Rating.class)))
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public static void getRatings(Wine wine, IFirebaseCallback<SparseIntArray> callback) {
        SparseIntArray ratings = new SparseIntArray();
        FirebaseFirestore.getInstance().collection(COLLECTION).whereEqualTo(FIELD_WINE_ID, wine.getId()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int rating = document.toObject(Rating.class).getRatingValue();
                            ratings.put(rating, ratings.get(rating, 0) + 1);
                        }
                    }
                    callback.onCallback(ratings);
                });
    }

    public static void set(Wine wine, int ratingValue, IFirebaseCallback<Rating> callback) {
        WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    Rating rating = new Rating(FirebaseAuth.getInstance().getUid(), wine.getId(), ratingValue);
                    getRatingReference(wine).set(rating, SetOptions.merge())
                            .addOnSuccessListener(aVoid1 -> callback.onCallback(rating))
                            .addOnFailureListener(e -> callback.onCallback(null));
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public static void delete(Wine wine, IFirebaseCallback<Rating> callback) {
        getRatingReference(wine).delete()
                .addOnSuccessListener(result -> callback.onCallback(null))
                .addOnFailureListener(result -> callback.onCallback(new Rating()));
    }

    public static void getCount(IFirebaseCallback<Integer> callback) {
        countCollectionItemsByUser(COLLECTION, callback);
    }

    private static DocumentReference getRatingReference(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION).document(wine.getId() + "-" + FirebaseAuth.getInstance().getUid());
    }
}