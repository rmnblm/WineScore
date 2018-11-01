package ch.hsr.winescore.ui.wine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.hsr.winescore.ui.utils.BaseViewHolder;
import ch.hsr.winescore.ui.utils.FirebaseRecyclerViewAdapter;
import ch.hsr.winescore.ui.utils.ListFragment;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.details.DetailsActivity;
import ch.hsr.winescore.data.repositories.WinesFirebaseRepository;

public class WinesFragment extends ListFragment<Wine> {

    public static final String ARGUMENT_QUERY_FIELD = "query_field";
    private String mQueryField;

    public static WinesFragment newInstance(String queryField) {
        WinesFragment fragment = new WinesFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_QUERY_FIELD, queryField);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQueryField = getArguments().getString(ARGUMENT_QUERY_FIELD);
        }
    }

    @Override
    protected Query getQuery() {
        return FirebaseFirestore.getInstance().collection(WinesFirebaseRepository.COLLECTION)
                .whereArrayContains(mQueryField, FirebaseAuth.getInstance().getUid());
    }

    @Override
    protected Class<Wine> getElementClass() {
        return Wine.class;
    }

    @Override
    protected FirebaseRecyclerViewAdapter<Wine, BaseViewHolder<Wine>> createAdapter(FirestorePagingOptions<Wine> options) {
        return new FirebaseRecyclerViewAdapter<>(this, options, R.layout.fragment_latest_listentry, WineViewHolder::newInstance);
    }

    @Override
    public void navigateToDetailScreen(View view, Wine item) {
        Context context = view.getContext();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ARGUMENT_WINE, item);
        context.startActivity(intent);
    }
}
