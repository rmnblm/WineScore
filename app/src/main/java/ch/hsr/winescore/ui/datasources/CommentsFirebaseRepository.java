package ch.hsr.winescore.ui.datasources;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import ch.hsr.winescore.model.Comment;
import ch.hsr.winescore.model.Wine;

public class CommentsFirebaseRepository {

    private static final String COLLECTION = "comments";
    private static final String FIELD_WINE_ID = "wineId";
    private static final String ANONYMOUS = "Anonymous";

    public static void add(Wine wine, String content, IFirebaseCallback<Comment> callback) {
        WinesFirebaseRepository.add(wine)
                .addOnSuccessListener(aVoid -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userName = user.getDisplayName() != null && !user.getDisplayName().equals("")
                            ? user.getDisplayName() : ANONYMOUS;
                    Comment comment = new Comment(user.getUid(), userName, wine.getId(), content);
                    getCommentReference(wine).set(comment)
                            .addOnSuccessListener(aVoid1 -> callback.onCallback(comment))
                            .addOnFailureListener(e -> callback.onCallback(null));
                })
                .addOnFailureListener(e -> callback.onCallback(null));
    }

    public static void delete(Wine wine, IFirebaseCallback<Comment> callback) {
        getCommentReference(wine).delete()
                .addOnSuccessListener(result -> callback.onCallback(null))
                .addOnFailureListener(result -> callback.onCallback(new Comment()));
    }

    public static Query getListQuery(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION).whereEqualTo(FIELD_WINE_ID, wine.getId());
    }

    private static DocumentReference getCommentReference(Wine wine) {
        return FirebaseFirestore.getInstance().collection(COLLECTION).document(wine.getId() + "-" + FirebaseAuth.getInstance().getUid());
    }
}