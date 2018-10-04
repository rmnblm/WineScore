package ch.hsr.winescore.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import ch.hsr.winescore.api.GWSClient;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.datasources.WineDataSourceFactory;
import ch.hsr.winescore.ui.adapters.WineOverviewAdapter;
import ch.hsr.winescore.ui.presenters.WineOverviewPresenter;
import ch.hsr.winescore.ui.views.WineOverviewView;
import ch.hsr.winescore.utils.BottomReachedListener;
import ch.hsr.winescore.utils.ItemClickListener;

public class WineOverviewActivity extends AppCompatActivity implements WineOverviewView {

    @BindView(R.id.cl_overview) CoordinatorLayout cl_overview;
    @BindView(R.id.swipe_container) SwipeRefreshLayout srl_swipe_container;
    @BindView(R.id.wine_list) RecyclerView rv_wine_list;

    private WineOverviewPresenter presenter;
    private WineOverviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_overview);
        ButterKnife.bind(this);

        setupToolbar();
        setupAdapter();
        setupRecyclerView();
        setupPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            View view = getLayoutInflater().inflate(R.layout.fragment_filter_sheet, null);
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(view);
            dialog.show();
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
        srl_swipe_container.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupPresenter() {
        presenter = new WineOverviewPresenter(createWineDataSourceFactory());
        presenter.attachView(this);
        presenter.getWines().observe(this, wines -> {
            adapter.submitList(wines);
            srl_swipe_container.setRefreshing(false);
        });
    }

    protected WineDataSourceFactory createWineDataSourceFactory() {
        return new WineDataSourceFactory(GWSClient.getService());
    }

    @Override
    public void showInitialLoading() {
        srl_swipe_container.setRefreshing(true);
    }

    @Override
    public void hideInitialLoading() {
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
