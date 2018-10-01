package ch.hsr.winescore.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private static final int REMAINING_ITEMS_RELOAD_THRESHOLD = 10;

    private int startingPage = 1;
    private int currentPage = 1;
    private int latestTotalItemCount = 0;
    private boolean isLoading = true;

    LinearLayoutManager layoutManager;

    public InfiniteScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // Note that lastVisibleItemPosition() is zero-based
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition() + 1;
        int totalItemCount = layoutManager.getItemCount();

        // Assume list was invalidated -- set back to default
        if (totalItemCount < latestTotalItemCount) {
            this.currentPage = this.startingPage;
            this.latestTotalItemCount = totalItemCount;
        }

        // If still loading and dataset size has been updated,
        // update load state and last item count
        if (isLoading && totalItemCount > latestTotalItemCount) {
            isLoading = false;
            latestTotalItemCount = totalItemCount;
        }

        // If not loading and within threshold limit, increase current page and load more data
        if (!isLoading && (lastVisibleItemPosition >= (totalItemCount - REMAINING_ITEMS_RELOAD_THRESHOLD))) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
