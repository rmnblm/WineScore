package ch.hsr.winescore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WineList {

    @SerializedName("results")
    @Expose
    private ArrayList<Wine> wines;

    @SerializedName("count")
    @Expose
    private Integer count;

    public WineList() {
        wines = new ArrayList<>();
    }

    /**
     * @return The wines
     */
    public ArrayList<Wine> getWines() {
        return wines;
    }

    /**
     * @return The total amount of wines
     */
    public Integer getCount() {
        return count;
    }
}
