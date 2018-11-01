package ch.hsr.winescore.ui.datasources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirebaseRepository {
    private static final String FIELD_USER_ID = "userId";

    public static void countCollectionItemsByUser(String collection, IFirebaseCallback<Integer> callback) {
        FirebaseFirestore.getInstance().collection(collection)
                .whereEqualTo(FIELD_USER_ID, FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshots -> callback.onCallback(documentSnapshots.size()))
                .addOnFailureListener(e -> callback.onCallback(0));
    }
}
