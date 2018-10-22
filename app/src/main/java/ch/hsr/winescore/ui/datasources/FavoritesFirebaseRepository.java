package ch.hsr.winescore.ui.datasources;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

import ch.hsr.winescore.model.Favorite;
import ch.hsr.winescore.model.Wine;

public class FavoritesFirebaseRepository {

    private static final String COLLECTION = "favorites";
    private static final String USER_ID = "userId";
    private static final String WINE_ID = "wineId";

    public static void add(Wine wine, IFirebaseCallback<Favorite> callback) {
        WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    Favorite favorite = new Favorite(FirebaseAuth.getInstance().getUid(), wine.getId());
                    getFavoriteReference(wine).set(favorite, SetOptions.merge())
                            .addOnSuccessListener(aVoid1 -> callback.onCallback(favorite))
                            .addOnFailureListener(e -> callback.onCallback(null));
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }


    public static void get(Wine wine, IFirebaseCallback<Favorite> callback) {
        getFavoriteReference(wine).get()
                .addOnSuccessListener(result -> callback.onCallback(result.toObject(Favorite.class)))
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public static void delete(Wine wine, IFirebaseCallback<Favorite> callback) {
        getFavoriteReference(wine).delete()
                .addOnSuccessListener(result -> callback.onCallback(null))
                .addOnFailureListener(result -> callback.onCallback(new Favorite()));
    }

    private static DocumentReference getFavoriteReference(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION).document(wine.getId() + "-" + FirebaseAuth.getInstance().getUid());
    }
}
