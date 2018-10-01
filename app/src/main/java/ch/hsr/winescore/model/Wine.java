package ch.hsr.winescore.model;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<String> getRegions() {
        return regions;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getConfidenceIndex() {
        return confidenceIndex;
    }

    public void setConfidenceIndex(String confidenceIndex) {
        this.confidenceIndex = confidenceIndex;
    }

}
