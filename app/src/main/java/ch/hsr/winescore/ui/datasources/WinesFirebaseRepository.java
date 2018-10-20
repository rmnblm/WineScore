package ch.hsr.winescore.ui.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import ch.hsr.winescore.model.Wine;

public class WinesFirebaseRepository {
    public static final String COLLECTION = "wines";

    public static Task<Void> add(Wine wine) {
        DocumentReference ref = FirebaseFirestore.getInstance().collection(COLLECTION).document(wine.getId());
        return ref.set(wine, SetOptions.merge());
    }
}
