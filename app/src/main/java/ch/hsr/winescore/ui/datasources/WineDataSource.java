package ch.hsr.winescore.ui.datasources;

import android.arch.paging.PositionalDataSource;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.api.GWSService;
import ch.hsr.winescore.api.responses.WineResponse;
import ch.hsr.winescore.model.DataLoadState;
import ch.hsr.winescore.model.Wine;
import ch.hsr.winescore.utils.DataLoadStateObserver;
import retrofit2.Call;
import retrofit2.Response;

public class WineDataSource extends PositionalDataSource<Wine> {

    public static final String TAG = WineDataSource.class.getSimpleName();

    private final GWSService apiService;
    private final DataLoadStateObserver observer;
    private final SharedPreferences preferences;

    private String color;
    private String country;
    private String vintage;
    private String ordering;

    public WineDataSource(GWSService apiService, DataLoadStateObserver observer) {
        this.apiService = apiService;
        this.observer = observer;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(WineScoreApplication.getApplicationInstance().getBaseContext());
    }

    public WineDataSource(GWSService apiService) {
        this(apiService, loadState -> {
            // Null object, do nothing
        });
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.INITIAL_LOADING);

        refreshParameters();

        Log.d(TAG, "[loadInitial] pageSize = " + params.pageSize +
                ", requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize);

        final Call<WineResponse> wineListCall = apiService.getLatest(params.pageSize, params.requestedStartPosition, color, country, vintage, ordering);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            callback.onResult(response.body().getWines(), params.requestedStartPosition);
            observer.onDataLoadStateChanged(DataLoadState.LOADED);
        } catch (IOException e) {
            Log.e(TAG, "Initial load failed", e);
            observer.onDataLoadStateChanged(DataLoadState.FAILED);
        }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Wine> callback) {
        observer.onDataLoadStateChanged(DataLoadState.LOADING);

        Log.d(TAG, "[loadRange] loadSize = " + params.loadSize + ", startPosition = " + params.startPosition);

        final Call<WineResponse> wineListCall = apiService.getLatest(params.loadSize, params.startPosition, color, country, vintage, ordering);

        try {
            // Execute call synchronously since function is called on a background thread
            Response<WineResponse> response = wineListCall.execute();
            callback.onResult(response.body().getWines());
            observer.onDataLoadStateChanged(DataLoadState.LOADED);
        } catch (IOException e) {
            Log.e(TAG, "Load range failed", e);
            observer.onDataLoadStateChanged(DataLoadState.FAILED);
        }
    }

    private void refreshParameters() {
        if (preferences != null) {
            String all = WineScoreApplication.getApplicationInstance().getString(R.string.array_all_value);
            color = getStringPreference("pref_color", all);
            country = getStringPreference("pref_country", all);
            vintage = getStringPreference("pref_vintage", "");
            ordering = getStringPreference("pref_ordering", "-date");
        }
    }

    private String getStringPreference(String key, String defaultValue) {
        String value = preferences.getString(key, defaultValue);
        return defaultValue.equals(value) ? null : value;
    }
    
}
