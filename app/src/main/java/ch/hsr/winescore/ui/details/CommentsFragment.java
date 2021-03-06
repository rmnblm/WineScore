package ch.hsr.winescore.ui.details;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.hsr.winescore.data.repositories.ICommentsRepository;
import ch.hsr.winescore.ui.utils.FirebaseListFragment;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.Query;

import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.BaseViewHolder;
import ch.hsr.winescore.ui.utils.FirebaseRecyclerViewAdapter;
import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;

public class CommentsFragment extends FirebaseListFragment<Comment> {

    public static final String ARGUMENT_WINE = "wine";

    private Wine mWine;
    private ICommentsRepository commentsRepo;

    public static CommentsFragment newInstance(Wine wine) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_WINE, wine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWine = (Wine) getArguments().getSerializable(ARGUMENT_WINE);
            commentsRepo = new CommentsFirebaseRepository();
        }
    }

    @Override
    protected Query getQuery() {
        return commentsRepo.getListQuery(mWine);
    }

    @Override
    protected Class<Comment> getElementClass() {
        return Comment.class;
    }

    @Override
    protected FirebaseRecyclerViewAdapter<Comment, BaseViewHolder<Comment>> createAdapter(FirestorePagingOptions<Comment> options) {
        return new FirebaseRecyclerViewAdapter<>(this, options, R.layout.fragment_comment_listentry, CommentViewHolder::newInstance);
    }

    @Override
    public void navigateToDetailScreen(View view, Comment item) {
        // empty
    }

    @Override
    public void winesUpdated(PagedList<Wine> wines) {
        // empty
    }
}
