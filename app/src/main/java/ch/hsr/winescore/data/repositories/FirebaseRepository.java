package ch.hsr.winescore.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirebaseRepository {
    private static final String FIELD_USER_ID = "userId";

    protected FirebaseRepository() {
        throw new IllegalStateException("Static class");
    }

    public static void countCollectionItemsByUser(String collection, ICallback<Integer> callback) {
        FirebaseFirestore.getInstance().collection(collection)
                .whereEqualTo(FIELD_USER_ID, FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshots -> callback.onCallback(documentSnapshots.size()))
                .addOnFailureListener(e -> callback.onCallback(0));
    }
}
