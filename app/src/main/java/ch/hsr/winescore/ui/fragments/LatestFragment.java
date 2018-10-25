package ch.hsr.winescore.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.activities.DetailsActivity;
import ch.hsr.winescore.ui.adapters.WineRecyclerViewAdapter;
import ch.hsr.winescore.ui.presenters.LatestPresenter;
import ch.hsr.winescore.ui.views.LatestView;

public class LatestFragment extends Fragment implements LatestView {

    public static final String TAG = "LatestFragment";

    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.wineList) RecyclerView wineList;

    private LatestPresenter presenter;
    private WineRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_latest, container, false);
        ButterKnife.bind(this, rootView);

        setupAdapter();
        setupRecyclerView();
        setupPresenter();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.getWines().removeObservers(this);
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
    public void showError(String errorMessage) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", v -> presenter.refreshData());
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
        adapter = new WineRecyclerViewAdapter(
                (view, position) -> presenter.listItemClicked(view, position),
                () -> swipeContainer.setRefreshing(true)
        );
    }

    private void setupRecyclerView() {
        wineList.setLayoutManager(new LinearLayoutManager(getActivity()));
        wineList.setHasFixedSize(true);
        wineList.setAdapter(adapter);
        wineList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Show loading indicator when bottom is reached before new data got loaded, e.g. slow API
                if (!recyclerView.canScrollVertically(1)) { showLoading(); }
            }
        });

        swipeContainer.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupPresenter() {
        presenter = new LatestPresenter();
        presenter.attachView(this);
        presenter.getWines().observe(this, wines -> adapter.submitList(wines));
        presenter.getLoadState().observe(this, loadState -> {
            if (loadState == DataLoadState.INITIAL_LOADING)
                showLoading();
            else if (loadState == DataLoadState.LOADED)
                hideLoading();
            else if (loadState == DataLoadState.FAILED)
                showError(getString(R.string.dataload_error_message));
        });
    }
}
