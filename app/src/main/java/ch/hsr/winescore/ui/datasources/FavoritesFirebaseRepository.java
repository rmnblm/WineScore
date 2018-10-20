package ch.hsr.winescore.ui.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.hsr.winescore.model.Favorite;
import ch.hsr.winescore.model.Wine;

public class FavoritesFirebaseRepository {

    public static final String COLLECTION = "favorites";

    public static Task<Void> add(Wine wine) {
        return WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    FirebaseFirestore.getInstance().collection(COLLECTION)
                            .add(new Favorite(FirebaseAuth.getInstance().getUid(), wine.getId()));
                });

    }
}
