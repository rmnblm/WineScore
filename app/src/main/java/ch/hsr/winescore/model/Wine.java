package ch.hsr.winescore.model;

import android.support.v7.util.DiffUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Wine implements Serializable {

    @SerializedName("wine")
    @Expose
    private String name;

    @SerializedName("wine_id")
    @Expose
    private String id;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("regions")
    @Expose
    private ArrayList<String> regions;

    @SerializedName("appellation")
    @Expose
    private String appellation;

    @SerializedName("vintage")
    @Expose
    private String vintage;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("wine_type")
    @Expose
    private String wineType;

    @SerializedName("score")
    @Expose
    private Double score;

    @SerializedName("confidence_index")
    @Expose
    private String confidenceIndex;

    public Wine(String name, String id) {
        this.name = name;
        this.id = id;
        this.regions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<String> getRegions() {
        return regions;
    }

    public String getAppellation() {
        return appellation;
    }

    public String getVintage() {
        return vintage;
    }

    public String getColor() {
        return color;
    }

    public String getWineType() {
        return wineType;
    }

    public Double getScore() {
        return score;
    }

    public String getConfidenceIndex() {
        return confidenceIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        Wine wine = (Wine) obj;
        return wine.id == this.id;
    }

    public static final DiffUtil.ItemCallback<Wine> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Wine>() {
                @Override
                public boolean areItemsTheSame(Wine oldItem, Wine newItem) {
                    return oldItem.getId() == newItem.getId();
                }
                @Override
                public boolean areContentsTheSame(Wine oldItem, Wine newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
