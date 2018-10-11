package ch.hsr.winescore.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.adapters.WineOverviewAdapter;
import ch.hsr.winescore.ui.presenters.WineOverviewPresenter;
import ch.hsr.winescore.ui.views.WineOverviewView;

public class WineOverviewActivity extends AppCompatActivity implements WineOverviewView {

    @BindView(R.id.cl_overview) CoordinatorLayout cl_overview;
    @BindView(R.id.swipe_container) SwipeRefreshLayout srl_swipe_container;
    @BindView(R.id.wine_list) RecyclerView rv_wine_list;

    private WineOverviewPresenter presenter;
    private WineOverviewAdapter adapter;
    private BottomSheetDialog filterDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_overview);
        ButterKnife.bind(this);

        setupToolbar();
        setupAdapter();
        setupRecyclerView();
        setupPresenter();
        setupBottomSheetDialog();
        setupPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    protected void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        super.onPause();
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

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    private void setupAdapter() {
        adapter = new WineOverviewAdapter(
                (view, position) -> presenter.listItemClicked(view, position),
                () -> srl_swipe_container.setRefreshing(true)
        );
    }

    private void setupRecyclerView() {
        rv_wine_list.setLayoutManager(new LinearLayoutManager(this));
        rv_wine_list.setHasFixedSize(true);
        rv_wine_list.setAdapter(adapter);
        rv_wine_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Show loading indicator when bottom is reached before new data got loaded, e.g. slow API
                if (!recyclerView.canScrollVertically(1)) { showLoading(); }
            }
        });

        srl_swipe_container.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupPresenter() {
        presenter = new WineOverviewPresenter();
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

    private void setupPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void setupBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null);
        filterDialog = new BottomSheetDialog(this);
        filterDialog.setContentView(view);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener =
            (sharedPreferences, key) -> presenter.refreshData();

    @Override
    public void showLoading() {
        srl_swipe_container.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        srl_swipe_container.setRefreshing(false);
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar snackbar = Snackbar.make(cl_overview, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", v -> presenter.refreshData());
        snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }

    @Override
    public void navigateToDetailScreen(View view, Wine wine) {
        Context context = view.getContext();
        Intent intent = new Intent(context, WineDetailActivity.class);
        intent.putExtra("wine", wine);
        context.startActivity(intent);
    }
}
