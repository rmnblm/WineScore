package ch.hsr.winescore.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
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
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.ui.adapters.WineOverviewAdapter;
import ch.hsr.winescore.ui.presenters.WineOverviewPresenter;
import ch.hsr.winescore.ui.views.WineOverviewView;
import ch.hsr.winescore.utils.ItemClickListener;

public class WineOverviewActivity extends AppCompatActivity implements WineOverviewView {

    @BindView(R.id.cl_overview) CoordinatorLayout cl_overview;
    @BindView(R.id.wine_list) RecyclerView rv_wine_list;

    private WineOverviewPresenter presenter;
    private WineOverviewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

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
        adapter = new WineOverviewAdapter(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.listItemClicked(view, position);
            }
        });
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        rv_wine_list.hasFixedSize();
        rv_wine_list.setLayoutManager(linearLayoutManager);
        rv_wine_list.setAdapter(adapter);
    }

    private void setupPresenter() {
        presenter = new WineOverviewPresenter();
        presenter.attachView(this);
        presenter.getWines().observe(this, new Observer<PagedList<Wine>>() {
            @Override
            public void onChanged(@Nullable PagedList<Wine> pagedList) {
                adapter.submitList(pagedList);
            }
        });
    }

    @Override
    public void navigateToDetailScreen(View view, Wine wine) {
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
