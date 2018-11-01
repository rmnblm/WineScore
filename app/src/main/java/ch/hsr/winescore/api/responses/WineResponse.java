package ch.hsr.winescore.api.responses;

import ch.hsr.winescore.model.Wine;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WineResponse {

    @SerializedName("results")
    @Expose
    private List<Wine> wines;

    @SerializedName("count")
    @Expose
    private Integer count;

    public WineResponse() {
        wines = new ArrayList<>();
        count = 0;
    }

    /**
     * @return The wines
     */
    public List<Wine> getWines() {
        return wines;
    }

    /**
     * @return The total amount of wines
     */
    public Integer getCount() {
        return count;
    }
}
