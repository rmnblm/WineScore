package ch.hsr.winescore.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.hsr.winescore.ui.utils.ListFragment;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.Query;

import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Comment;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.utils.BaseViewHolder;
import ch.hsr.winescore.ui.utils.FirebaseRecyclerViewAdapter;
import ch.hsr.winescore.data.repositories.CommentsFirebaseRepository;

public class CommentsFragment extends ListFragment<Comment> {

    public static final String ARGUMENT_WINE = "wine";
    private Wine mWine;

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
        }
    }

    @Override
    protected Query getQuery() {
        return CommentsFirebaseRepository.getListQuery(mWine);
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
}
