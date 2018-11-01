package ch.hsr.winescore.ui.fragments;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.ui.adapters.BaseViewHolder;
import ch.hsr.winescore.ui.adapters.FirebaseRecyclerViewAdapter;
import ch.hsr.winescore.ui.views.ListView;

public abstract class ListFragment<T> extends Fragment implements ListView<T> {

    @BindView(R.id.layout)
    View layout;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyDataStore)
    View emptyDataView;

    private FirebaseRecyclerViewAdapter<T, BaseViewHolder<T>> adapter;

    protected abstract Query getQuery();
    protected abstract Class<T> getElementClass();
    protected abstract FirebaseRecyclerViewAdapter<T, BaseViewHolder<T>> createAdapter(FirestorePagingOptions<T> options);

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


    private void setupAdapter() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(FirebaseRecyclerViewAdapter.PAGE_SIZE)
                .build();
        FirestorePagingOptions<T> options = new FirestorePagingOptions.Builder<T>()
                .setLifecycleOwner(this)
                .setQuery(getQuery(), config, getElementClass())
                .build();
        adapter = createAdapter(options);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        swipeContainer.setOnRefreshListener(() -> adapter.refresh());
    }

}
