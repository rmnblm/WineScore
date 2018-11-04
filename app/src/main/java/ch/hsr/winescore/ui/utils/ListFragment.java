package ch.hsr.winescore.ui.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import butterknife.BindView;
import ch.hsr.winescore.R;

public abstract class ListFragment<T> extends Fragment implements ListView<T> {

    @BindView(R.id.layout) View layout;
    @BindView(R.id.swipeContainer) protected SwipeRefreshLayout swipeContainer;
    @BindView(R.id.emptyDataStore) View viewEmptyData;

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
        viewEmptyData.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyState() {
        viewEmptyData.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        Snackbar snackbar = Snackbar.make(layout, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.dataload_error_retry, v -> onClickErrorAction());
        snackbar.getView().setBackgroundResource(R.color.colorErrorMessage);
        snackbar.setActionTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        snackbar.show();
    }

    protected abstract void onClickErrorAction();
}
