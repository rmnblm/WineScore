package ch.hsr.winescore.ui.adapters;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Comment;
import ch.hsr.winescore.ui.views.ListView;

public class FirebaseCommentsRecyclerViewAdapter extends FirestorePagingAdapter<Comment, CommentViewHolder> {

    public static final int PAGE_SIZE = 20;
    private final ListView view;

    public FirebaseCommentsRecyclerViewAdapter(ListView view, @NonNull FirestorePagingOptions<Comment> options) {
        super(options);
        this.view = view;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_comment_listentry, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
        holder.bindTo(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state) {
            case LOADING_INITIAL:
            case LOADING_MORE:
                view.showLoading();
                break;
            case LOADED:
            case FINISHED:
                view.hideLoading();
                if (getItemCount() == 0) {
                    view.showEmptyState();
                }
                break;
            case ERROR:
                view.showError();
                break;
        }
    }

    public void refresh() {
        PagedList<DocumentSnapshot> list = getCurrentList();
        if (list != null) {
            list.getDataSource().invalidate();
        }
    }
}
