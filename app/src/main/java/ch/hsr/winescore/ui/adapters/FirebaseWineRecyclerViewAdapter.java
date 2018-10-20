package ch.hsr.winescore.ui.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;

import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.views.ListView;

public class FirebaseWineRecyclerViewAdapter extends FirestorePagingAdapter<Wine, WineViewHolder> {

    public static final int PAGE_SIZE = 50;
    private final ListView view;

    public FirebaseWineRecyclerViewAdapter(ListView view, @NonNull FirestorePagingOptions<Wine> options) {
        super(options);
        this.view = view;
    }

    @NonNull
    @Override
    public WineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_explore_listentry, parent, false);
        return new WineViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull WineViewHolder holder, int position, @NonNull Wine model) {
        holder.bindTo(model);
        holder.layout.setOnClickListener(v -> view.navigateToDetailScreen(v, model));
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
                break;
            case ERROR:
                view.showError();
                break;
        }
    }
}
