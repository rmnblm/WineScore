package ch.hsr.winescore.ui.presenters;

import android.view.View;
import ch.hsr.winescore.api.GlobalWineScoreApi;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.model.WineList;
import ch.hsr.winescore.ui.views.WineOverviewView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WineOverviewPresenter implements Presenter<WineOverviewView> {

    private static final int PER_PAGE_LIMIT = 30;

    private WineOverviewView view;
    private Set<Call<WineList>> wineListCalls;

    @Override
    public void attachView(WineOverviewView view) {
        this.view = view;
        this.wineListCalls = new HashSet<>();
    }

    public void subscribe() {
        view.showWineList();
    }

    public void unsubscribe() {
        for (Call<WineList> call : wineListCalls) {
            if (call != null && call.isExecuted() && !call.isCanceled()) {
                call.cancel();
            }
        }
        wineListCalls.clear();
    }

    /**
     * Updates the wines based on the specified page.
     * @param page One-based page index
     */
    public void updateWines(int page) {
        view.showLoading();

        final Call<WineList> wineListCall = GlobalWineScoreApi
                .getService()
                .getLatest(PER_PAGE_LIMIT, (page - 1) * PER_PAGE_LIMIT);

        wineListCalls.add(wineListCall);
        wineListCall.enqueue(new Callback<WineList>() {
            @Override
            public void onResponse(Call<WineList> call, Response<WineList> response) {

                if (response.isSuccessful()) {
                    List<Wine> wines = response.body().getWines();
                    view.hideLoading();
                    view.showAddedWines(wines);
                }
            }

            @Override
            public void onFailure(Call<WineList> call, Throwable t) {
                view.showError("error"); // TODO: Localize this
            }
        });
    }

    public void listItemClicked(View v, int position) {
        view.navigateToDetailScreen(v, position);
    }
}
