package ch.hsr.winescore.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.details.DetailsActivity;

public class SearchActivity extends AppCompatActivity implements SearchView {

    @BindView(R.id.searchEditText) EditText inputSearch;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.wineList) RecyclerView rvWineList;
    @BindView(R.id.emptyDataStore) View viewEmptyData;

    private SearchPresenter presenter;
    private BottomSheetDialog filterDialog;
    private boolean didFirstSearch = false;


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

    @Override
    protected void onDestroy() {
        presenter.clearPreferences();
        super.onDestroy();
    }

    private void setupPresenter() {
        presenter = new SearchPresenter();
        presenter.attachView(this);
        presenter.bindLoadState(this);
    }

    private void setupRecyclerView() {
        rvWineList.setLayoutManager(new LinearLayoutManager(this));
        rvWineList.setHasFixedSize(true);
        rvWineList.setAdapter(presenter.getAdapter());
        rvWineList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.scrollStateChanged(recyclerView.canScrollVertically(1));
            }
        });

        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupSearchbox() {
        inputSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = inputSearch.getText().toString();
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

        if (didFirstSearch) {
            presenter.refreshData();
        } else {
            presenter.bindWines(this);
            didFirstSearch = true;
        }
    }

    @OnClick(R.id.clearSearchButton)
    public void clearSearch(View animationSource) {
        inputSearch.setText("");
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
        if (didFirstSearch) {
            viewEmptyData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyState() {
        viewEmptyData.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dataload_error_retry, v -> presenter.refreshData());
        snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
        snackbar.setActionTextColor(ContextCompat.getColor(this, android.R.color.white));
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
