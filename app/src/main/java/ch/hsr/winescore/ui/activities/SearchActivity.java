package ch.hsr.winescore.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.presenters.SearchPresenter;
import ch.hsr.winescore.ui.views.SearchView;

public class SearchActivity extends AppCompatActivity implements SearchView {

    @BindView(R.id.searchEditText) EditText searchEditText;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.wineList) RecyclerView wineList;

    private SearchPresenter presenter;
    private BottomSheetDialog filterDialog;

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

        setupPresenter();
        setupSearchbox();
        setupRecyclerView();
        setupFilterDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            filterDialog.show();
            return true;
        }
        return false;
    }

    private void setupPresenter() {
        presenter = new SearchPresenter();
        presenter.attachView(this);
        presenter.bindLoadState(this);
        presenter.bindWines(this);
    }

    private void setupRecyclerView() {
        wineList.setLayoutManager(new LinearLayoutManager(this));
        wineList.setHasFixedSize(true);
        wineList.setAdapter(presenter.getAdapter());
        wineList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.reachedEndOfList(recyclerView.canScrollVertically(1));
            }
        });

        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(() -> presenter.refreshData());
        swipeContainer.setVisibility(View.INVISIBLE);
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

    private void setupFilterDialog() {
        View view = getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null);
        filterDialog = new BottomSheetDialog(this);
        filterDialog.setContentView(view);
        filterDialog.setOnDismissListener(dialog -> presenter.refreshData());
    }

    private void handleSearch(String query) {
        presenter.setSearchQuery(query);
        presenter.refreshData();
        swipeContainer.setVisibility(View.VISIBLE);
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
