package ch.hsr.winescore.ui.fragments;

import android.arch.paging.PagedList;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Comment;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.adapters.CommentViewHolder;
import ch.hsr.winescore.ui.adapters.FirebaseCommentsRecyclerViewAdapter;
import ch.hsr.winescore.ui.datasources.CommentsFirebaseRepository;
import ch.hsr.winescore.ui.views.ListView;

public class CommentsFragment extends Fragment implements ListView {

    public static final String ARGUMENT_WINE = "WINE";

    @BindView(R.id.layout)
    View layout;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyDataStore)
    View emptyDataView;

    private FirestorePagingAdapter<Comment, CommentViewHolder> adapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

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
    public void navigateToDetailScreen(View view, Wine wine) { }

    private void setupAdapter() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(FirebaseCommentsRecyclerViewAdapter.PAGE_SIZE)
                .build();

        FirestorePagingOptions<Comment> options = new FirestorePagingOptions.Builder<Comment>()
                .setLifecycleOwner(this)
                .setQuery(CommentsFirebaseRepository.getListQuery(mWine), config, Comment.class)
                .build();

        adapter = new FirebaseCommentsRecyclerViewAdapter(this, options);
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
