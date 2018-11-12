package ch.hsr.winescore.data.repositories;

import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Wine;

public interface ICommentsRepository {
    void add(Wine wine, String content, ICallback<Comment> callback);
    void getLast(Wine wine, ICallback<Comment> callback);
    void getCount(ICallback<Integer> callback);
}
