package ch.hsr.winescore.data.repositories;

import com.google.firebase.firestore.Query;

import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Wine;

public class CommentsRepositoryMock implements ICommentsRepository {

    public Wine commented;
    public Comment comment;

    @Override
    public void add(Wine wine, String content, ICallback<Comment> callback) {

    }

    @Override
    public void getLast(Wine wine, ICallback<Comment> callback) {
        callback.onCallback(comment);
    }

    @Override
    public void getCount(ICallback<Integer> callback) {
        callback.onCallback(0);
    }

    @Override
    public Query getListQuery(Wine wine) {
        return null;
    }
}
