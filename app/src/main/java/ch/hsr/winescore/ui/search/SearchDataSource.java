package ch.hsr.winescore.ui.search;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import ch.hsr.winescore.R;
import ch.hsr.winescore.WineScoreApplication;
import ch.hsr.winescore.data.api.GWSService;
import ch.hsr.winescore.data.api.responses.WineResponse;
import ch.hsr.winescore.ui.wine.WineDataSourceBase;
import ch.hsr.winescore.domain.utils.DataLoadStateObserver;
import retrofit2.Call;

public class SearchDataSource extends WineDataSourceBase {

    private final SharedPreferences preferences;

    private String query;
    private String color;
    private String country;
    private String vintage;
    private String ordering;

    public SearchDataSource(GWSService apiService, DataLoadStateObserver observer) {
        super(apiService, observer);
        this.preferences = WineScoreApplication.getSharedPreferences();
    }

    @Override
    protected Call<WineResponse> getLoadInitialCall(@NonNull LoadInitialParams params) {
        refreshParameters();
        return apiService.searchBy(query, color, country, vintage, ordering, params.pageSize, 0);
    }

    @Override
    protected Call<WineResponse> getLoadRangeCall(@NonNull LoadRangeParams params) {
        return apiService.searchBy(query, color, country, vintage, ordering, params.loadSize, params.startPosition);
    }

    private void refreshParameters() {
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
