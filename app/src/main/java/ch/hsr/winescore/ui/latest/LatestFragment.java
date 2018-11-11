package ch.hsr.winescore.ui.latest;

import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.hsr.winescore.R;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.ui.details.DetailsActivity;
import ch.hsr.winescore.ui.utils.ListFragment;
import ch.hsr.winescore.ui.wine.WineRecyclerViewAdapter;

public class LatestFragment extends ListFragment<Wine> {

    @BindView(R.id.recyclerView) RecyclerView rvWineList;

    private LatestPresenter presenter;
    private WineRecyclerViewAdapter adapter;
    private BottomSheetDialog filterDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        setupPresenter();
        setupAdapter();
        setupRecyclerView();
        setupFilterDialog();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
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
    public void onDestroy() {
        presenter.clearPreferences();
        super.onDestroy();
    }

    private void setupAdapter() {
        this.adapter = new WineRecyclerViewAdapter(presenter::listItemClicked);
    }

    private void setupRecyclerView() {
        rvWineList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWineList.setHasFixedSize(true);
        rvWineList.setAdapter(adapter);
        rvWineList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvWineList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                presenter.scrollStateChanged(recyclerView.canScrollVertically(1));
            }
        });

        swipeContainer.setOnRefreshListener(() -> presenter.refreshData());
    }

    private void setupPresenter() {
        presenter = getPresenter();
        presenter.attachView(this);
        presenter.bindLoadState(this);
        presenter.bindWines(this);
    }

    protected LatestPresenter getPresenter() {
        return new LatestPresenter();
    }

    @Override
    protected void onClickErrorAction() {
        presenter.refreshData();
    }

    @Override
    public void navigateToDetailScreen(View view, Wine wine) {
        Context context = view.getContext();
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(DetailsActivity.ARGUMENT_WINE, wine);
        context.startActivity(intent);
    }

    @Override
    public void winesUpdated(PagedList<Wine> wines) {
        adapter.submitList(wines);
    }

    private void setupFilterDialog() {
        View view = getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null);
        filterDialog = new BottomSheetDialog(getContext());
        filterDialog.setContentView(view);
        filterDialog.setOnDismissListener(dialog -> presenter.refreshData());
    }
}
