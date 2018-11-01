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

import ch.hsr.winescore.ui.views.ListView;

public class FirebaseRecyclerViewAdapter<TElement, TViewHolder extends BaseViewHolder<TElement>> extends FirestorePagingAdapter<TElement, TViewHolder> {

    public static final int PAGE_SIZE = 20;
    private final ListView<TElement> mView;
    private final int mListEntryLayout;
    private final ViewHolderCreator<TViewHolder> mViewHolderCreator;

    public FirebaseRecyclerViewAdapter(ListView view, @NonNull FirestorePagingOptions<TElement> options, int listEntryLayout, ViewHolderCreator<TViewHolder> creator) {
        super(options);
        this.mView = view;
        this.mListEntryLayout = listEntryLayout;
        this.mViewHolderCreator = creator;
    }

    @NonNull
    @Override
    public TViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mListEntryLayout, parent, false);
        return mViewHolderCreator.createViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull TViewHolder holder, int position, @NonNull TElement model) {
        holder.bindTo(model);
        holder.itemView.setOnClickListener(v -> mView.navigateToDetailScreen(v, model));
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state) {
            case LOADING_INITIAL:
            case LOADING_MORE:
                mView.showLoading();
                break;
            case LOADED:
            case FINISHED:
                mView.hideLoading();
                if (getItemCount() == 0) {
                    mView.showEmptyState();
                } else {
                    mView.hideEmptyState();
                }
                break;
            case ERROR:
                mView.hideLoading();
                mView.showError();
                break;
        }
    }

    public void refresh() {
        PagedList<DocumentSnapshot> list = getCurrentList();
        if (list != null) {
            list.getDataSource().invalidate();
        }
    }

    public interface ViewHolderCreator<T> {
        T createViewHolder(View view);
    }
}
