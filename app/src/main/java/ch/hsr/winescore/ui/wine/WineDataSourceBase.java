package ch.hsr.winescore.ui.wine;

import android.arch.paging.PositionalDataSource;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.data.api.GWSService;
import ch.hsr.winescore.data.api.responses.WineResponse;
import ch.hsr.winescore.domain.models.Wine;
import ch.hsr.winescore.domain.utils.DataLoadState;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class WineDataSourceBase extends PositionalDataSource<Wine> {

    private static final String TAG = WineDataSourceBase.class.getSimpleName();

    protected final GWSService apiService;
    private final DataLoadStateObserver observer;
    private final SharedPreferences preferences;

    protected String query;
    protected String color;
    protected String country;
    protected String vintage;
    protected String ordering;

    private int totalCount = 0;

    public WineDataSourceBase(GWSService apiService, DataLoadStateObserver observer) {
        this.apiService = apiService;
        this.observer = observer;
        this.preferences = WineScoreApplication.getSharedPreferences();
    }

    protected abstract Call<WineResponse> getLoadInitialCall(@NonNull LoadInitialParams params);
    protected abstract Call<WineResponse> getLoadRangeCall(@NonNull LoadRangeParams params);

    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.INITIAL_LOADING);

        final Call<WineResponse> call = getLoadInitialCall(params);

        call.enqueue(new Callback<WineResponse>() {
            @Override
            public void onResponse(Call<WineResponse> call, Response<WineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalCount = response.body().getCount();
                    callback.onResult(response.body().getWines(), 0, response.body().getCount());
                    observer.onDataLoadStateChanged(DataLoadState.LOADED);
                } else {
                    Log.e(TAG, "Error on loadInitial");
                    observer.onDataLoadStateChanged(DataLoadState.FAILED);
                }
            }

            @Override
            public void onFailure(Call<WineResponse> call, Throwable t) {
                Log.e(TAG, "Error on loadInitial", t);
                observer.onDataLoadStateChanged(DataLoadState.FAILED);
            }
        });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.LOADING);

        final Call<WineResponse> call = getLoadRangeCall(params);

        call.enqueue(new Callback<WineResponse>() {
            @Override
            public void onResponse(Call<WineResponse> call, Response<WineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body().getWines());
                    observer.onDataLoadStateChanged(DataLoadState.LOADED);
                } else {
                    Log.e(TAG, "Error on loadRange");
                    observer.onDataLoadStateChanged(DataLoadState.FAILED);
                }
            }

            @Override
            public void onFailure(Call<WineResponse> call, Throwable t) {
                Log.e(TAG, "Error on loadRange", t);
                observer.onDataLoadStateChanged(DataLoadState.FAILED);
            }
        });
    }

    protected void refreshParameters() {
        if (preferences != null) {
            String all = WineScoreApplication.getApplicationInstance().getString(R.string.array_all_value);
            color = getStringPreference("pref_color", all);
            country = getStringPreference("pref_country", all);
            vintage = getStringPreference("pref_vintage", "");
            ordering = getStringPreference("pref_ordering", "-date");
            query = getStringPreference("pref_search_query", "");
        }
    }

    private String getStringPreference(String key, String defaultValue) {
        String value = preferences.getString(key, defaultValue);
        return defaultValue.equals(value) ? null : value;
    }

}
