package ch.hsr.winescore.ui.utils;

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
import ch.hsr.winescore.WineScoreApplication;

public class FirebaseRecyclerViewAdapter<T1, T2 extends BaseViewHolder<T1>> extends FirestorePagingAdapter<T1, T2> {

    public static final int PAGE_SIZE = 20;
    private final ListView<T1> mView;
    private final int mListEntryLayout;
    private final ViewHolderCreator<T2> mViewHolderCreator;

    public FirebaseRecyclerViewAdapter(ListView<T1> view, @NonNull FirestorePagingOptions<T1> options, int listEntryLayout, ViewHolderCreator<T2> creator) {
        super(options);
        this.mView = view;
        this.mListEntryLayout = listEntryLayout;
        this.mViewHolderCreator = creator;
    }

    @NonNull
    @Override
    public T2 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(mListEntryLayout, parent, false);
        return mViewHolderCreator.createViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull T2 holder, int position, @NonNull T1 model) {
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
                mView.showError(WineScoreApplication.getResourcesString(R.string.dataload_error_message));
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
