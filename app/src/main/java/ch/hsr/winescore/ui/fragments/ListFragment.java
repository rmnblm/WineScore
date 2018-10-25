package ch.hsr.winescore.ui.fragments;

import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.activities.DetailsActivity;
import ch.hsr.winescore.ui.adapters.FirebaseWineRecyclerViewAdapter;
import ch.hsr.winescore.ui.adapters.WineViewHolder;
import ch.hsr.winescore.ui.datasources.WinesFirebaseRepository;
import ch.hsr.winescore.ui.views.ListView;

public class ListFragment extends Fragment implements ListView {

    public static final String TITLE = "title";
    public static final String QUERY_FIELD = "query_field";
    @BindView(R.id.layout)
    View layout;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyDataStore)
    View emptyDataView;

    private FirestorePagingAdapter<Wine, WineViewHolder> adapter;
    int mTitle;
    String mQueryField;

    public static ListFragment newInstance(int title, String queryField) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putString(QUERY_FIELD, queryField);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getInt(TITLE);
            mQueryField = getArguments().getString(QUERY_FIELD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(mTitle);
        setupAdapter();
        setupRecyclerView();

        return rootView;
    }

    @Override
    public void showLoading() {
        swipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void showEmptyState() {
        emptyDataView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyState() {
        emptyDataView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        Snackbar snackbar = Snackbar.make(layout, getString(R.string.dataload_error_message), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dataload_error_retry, v -> adapter.retry());
        snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }

    @Override
    public void navigateToDetailScreen(View view, Wine wine) {
        Context context = view.getContext();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("wine", wine);
        context.startActivity(intent);
    }

    private void setupAdapter() {
        CollectionReference wineCollection = FirebaseFirestore.getInstance().collection(WinesFirebaseRepository.COLLECTION);
        Query query = wineCollection.whereArrayContains(mQueryField, FirebaseAuth.getInstance().getUid());

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(FirebaseWineRecyclerViewAdapter.PAGE_SIZE)
                .build();

        FirestorePagingOptions<Wine> options = new FirestorePagingOptions.Builder<Wine>()
                .setLifecycleOwner(this)
                .setQuery(query, config, Wine.class)
                .build();

        adapter = new FirebaseWineRecyclerViewAdapter(this, options);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) { showLoading(); }
            }
        });

        swipeContainer.setOnRefreshListener(() -> adapter.retry());
    }

}
