package ch.hsr.winescore.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.adapters.WineItemAdapter;
import ch.hsr.winescore.ui.presenters.WineOverviewPresenter;
import ch.hsr.winescore.ui.views.WineOverviewView;
import ch.hsr.winescore.utils.InfiniteScrollListener;
import ch.hsr.winescore.utils.ItemClickListener;

import java.util.List;

public class WineOverviewActivity extends AppCompatActivity implements WineOverviewView {

    public static final String TAG = WineOverviewActivity.class.getSimpleName();

    @BindView(R.id.cl_overview) CoordinatorLayout cl_overview;
    @BindView(R.id.wine_list) RecyclerView rv_wine_list;

    private WineOverviewPresenter presenter;
    private WineItemAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_overview);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        setupAdapter();
        setupRecyclerView();
        setupPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
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

    private void setupAdapter() {
        adapter = new WineItemAdapter(this, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.listItemClicked(view, position);
            }
        });
    }

    private void setupPresenter() {
        presenter = new WineOverviewPresenter();
        presenter.attachView(this);
        presenter.subscribe();
        presenter.updateWines(0);
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);

        rv_wine_list.hasFixedSize();
        rv_wine_list.setLayoutManager(linearLayoutManager);

        infiniteScrollListener = new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.updateWines(page);
            }
        };
        rv_wine_list.addOnScrollListener(infiniteScrollListener);
    }

    @Override
    public void showWineList() {
        rv_wine_list.setAdapter(adapter);
    }

    @Override
    public void showAddedWines(List<Wine> wines) {
        adapter.addWines(wines);
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "*** Loading wines from server.");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "*** Done loading.");
    }

    @Override
    public void showError(String message) {
        Snackbar snackbar = Snackbar.make(cl_overview, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.updateWines(1);
                    }
                });
        snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
        snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }

    @Override
    public void navigateToDetailScreen(View view, int position) {
        Wine wine = adapter.getWines().get(position);
        Context context = view.getContext();
        Intent intent = new Intent(context, WineDetailActivity.class);
        intent.putExtra("wine", wine);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
