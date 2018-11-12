package ch.hsr.winescore.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Wine;

public class CommentsFirebaseRepository extends FirebaseRepository implements ICommentsRepository {

    private static final String COLLECTION = "comments";
    private static final String FIELD_WINE_ID = "wineId";
    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String ANONYMOUS = "Anonymous";

    public void add(Wine wine, String content, ICallback<Comment> callback) {
        WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userName = user.getDisplayName() != null && !user.getDisplayName().equals("")
                                ? user.getDisplayName() : ANONYMOUS;
                        Comment comment = new Comment(user.getUid(), userName, wine.getId(), content);
                        FirebaseFirestore.getInstance().collection(COLLECTION).add(comment)
                                .addOnSuccessListener(x -> callback.onCallback(comment))
                                .addOnFailureListener(e -> callback.onCallback(null));
                    } else {
                        callback.onCallback(null);
                    }

                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public void getLast(Wine wine, ICallback<Comment> callback) {
        getListQuery(wine).limit(1).get()
                .addOnSuccessListener(documentSnapshots -> {
                    for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                        callback.onCallback(document.toObject(Comment.class));
                    }
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public void getCount(ICallback<Integer> callback) {
        countCollectionItemsByUser(COLLECTION, callback);
    }

    public Query getListQuery(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION)
                .whereEqualTo(FIELD_WINE_ID, wine.getId())
                .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING);
    }

}