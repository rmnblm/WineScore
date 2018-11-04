package ch.hsr.winescore.ui.latest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.details.DetailsActivity;

public class LatestFragment extends Fragment implements LatestView {

    @BindView(R.id.layout) View layout;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recyclerView) RecyclerView wineList;
    @BindView(R.id.emptyDataStore)
    View emptyDataView;

    private LatestPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);

        setupPresenter();
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
    public void showError(String errorMessage) {
        Snackbar snackbar = Snackbar.make(layout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", v -> presenter.refreshData());
        snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }

    @Override
    public void navigateToDetailScreen(View view, Wine wine) {
        Context context = view.getContext();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ARGUMENT_WINE, wine);
        context.startActivity(intent);
    }

    private void setupRecyclerView() {
        wineList.setLayoutManager(new LinearLayoutManager(getActivity()));
        wineList.setHasFixedSize(true);
        wineList.setAdapter(presenter.getAdapter());
        wineList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.scrollStateChanged(recyclerView.canScrollVertically(1));
            }
        });

        swipeContainer.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupPresenter() {
        presenter = new LatestPresenter();
        presenter.attachView(this);
        presenter.bindLoadState(this);
        presenter.bindWines(this);
    }
}
