package ch.hsr.winescore.ui.datasources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ch.hsr.winescore.model.Comment;
import ch.hsr.winescore.model.Wine;

public class CommentsFirebaseRepository {

    private static final String COLLECTION = "comments";
    private static final String FIELD_WINE_ID = "wineId";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String ANONYMOUS = "Anonymous";

    private CommentsFirebaseRepository() {
        throw new IllegalStateException("Static class");
    }

    public static void add(Wine wine, String content, IFirebaseCallback<Comment> callback) {
        WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userName = user.getDisplayName() != null && !user.getDisplayName().equals("")
                            ? user.getDisplayName() : ANONYMOUS;
                    Comment comment = new Comment(user.getUid(), userName, wine.getId(), content);
                    FirebaseFirestore.getInstance().collection(COLLECTION).add(comment)
                            .addOnSuccessListener(x -> callback.onCallback(comment))
                            .addOnFailureListener(e -> callback.onCallback(null));
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public static void getLast(Wine wine, IFirebaseCallback<Comment> callback) {
        getListQuery(wine).limit(1).get()
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                        callback.onCallback(document.toObject(Comment.class));
                    }
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public static Query getListQuery(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION)
                .whereEqualTo(FIELD_WINE_ID, wine.getId())
                .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING);
    }

}