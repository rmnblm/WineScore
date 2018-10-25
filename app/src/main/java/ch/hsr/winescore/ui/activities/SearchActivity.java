package ch.hsr.winescore.ui.activities;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.adapters.WineRecyclerViewAdapter;
import ch.hsr.winescore.ui.presenters.SearchPresenter;
import ch.hsr.winescore.ui.views.SearchView;

public class SearchActivity extends AppCompatActivity implements SearchView {

    @BindView(R.id.searchEditText) EditText searchEditText;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.wineList) RecyclerView wineList;

    private SearchPresenter presenter;
    private WineRecyclerViewAdapter adapter;

    private boolean isObservingWines = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            handleSearch(query);
        }

        setupSearchbox();
        setupAdapter();
        setupRecyclerView();
        setupPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.getWines().removeObservers(this);
    }

    private void setupPresenter() {
        presenter = new SearchPresenter();
        presenter.attachView(this);
        presenter.getLoadState().observe(this, loadState -> {
            if (loadState == DataLoadState.INITIAL_LOADING)
                showLoading();
            else if (loadState == DataLoadState.LOADED)
                hideLoading();
            else if (loadState == DataLoadState.FAILED)
                showError(getString(R.string.dataload_error_message));
        });
    }

    private void setupAdapter() {
        adapter = new WineRecyclerViewAdapter(
                (view, position) -> presenter.listItemClicked(view, position),
                () -> swipeContainer.setRefreshing(true)
        );
    }

    private void setupRecyclerView() {
        wineList.setLayoutManager(new LinearLayoutManager(this));
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

        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupSearchbox() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchEditText.getText().toString();
                handleSearch(query);
            }
            return false;
        });
    }

    private void handleSearch(String query) {
        presenter.setSearchQuery(query);
        swipeContainer.setVisibility(View.VISIBLE);

        if (!isObservingWines) {
            // Setup the observer when searching for the first time
            presenter.getWines().observe(this, wines -> adapter.submitList(wines));
            isObservingWines = true;
        } else {
            presenter.refreshData();
        }
    }

    @OnClick(R.id.clearSearchButton)
    public void clearSearch(View animationSource) {
        searchEditText.setText("");
        swipeContainer.setVisibility(View.INVISIBLE);
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
}
