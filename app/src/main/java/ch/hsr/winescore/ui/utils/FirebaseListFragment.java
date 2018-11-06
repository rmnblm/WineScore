package ch.hsr.winescore.ui.utils;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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

public abstract class FirebaseListFragment<T> extends ListFragment<T> {

    @BindView(R.id.recyclerView) RecyclerView rvItemsList;

    private FirebaseRecyclerViewAdapter<T, BaseViewHolder<T>> adapter;

    protected abstract Query getQuery();
    protected abstract Class<T> getElementClass();
    protected abstract FirebaseRecyclerViewAdapter<T, BaseViewHolder<T>> createAdapter(FirestorePagingOptions<T> options);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

        setupAdapter();
        setupRecyclerView();
        return rootView;
    }

    @Override
    protected void onClickErrorAction() {
        adapter.retry();
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
        rvItemsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvItemsList.setAdapter(adapter);
        rvItemsList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        swipeContainer.setOnRefreshListener(() -> adapter.refresh());
    }

}
