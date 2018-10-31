package ch.hsr.winescore.model;

import android.support.v7.util.DiffUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wine implements Serializable {

    @SerializedName("wine")
    @Expose
    private String name;

    @SerializedName("wine_id")
    @Expose
    private String wineId;

    @SerializedName("lwin_11")
    @Expose
    private String lwin_11;

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
    private WineColor color;

    @SerializedName("wine_type")
    @Expose
    private WineType type;

    @SerializedName("score")
    @Expose
    private Double score;

    @SerializedName("confidence_index")
    @Expose
    private String confidenceIndex;

    private String shortName;
    private String winery;

    public Wine() { }

    public Wine(String name, String wineId) {
        this.name = name;
        this.wineId = wineId;
        this.regions = new ArrayList<>();
    }

    public String getWinery() {
        if (winery == null) {
            computeAdditionalProperties();
        }
        return winery;
    }

    public String getShortName() {
        if (shortName == null) {
            computeAdditionalProperties();
        }
        return shortName;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return lwin_11 != null ? lwin_11 : wineId + "-" + vintage;
    }

    public String getWineId() {
        return wineId;
    }

    public String getLwin_11() {
        return lwin_11;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getRegions() {
        return regions;
    }

    public String getAppellation() {
        return appellation;
    }

    public String getVintage() {
        return vintage;
    }

    public WineColor getColor() {
        return color;
    }

    public WineType getWineType() {
        return type;
    }

    public Double getScore() {
        return score;
    }

    public String getConfidenceIndex() {
        return confidenceIndex;
    }

    private void computeAdditionalProperties() {
        // Set default values
        winery = name;
        shortName = name;

        // The name typically defaults to the following parts:
        // First:                       The name of the chateau/winery/etc
        // Second:                      The actual name of the wine
        // Between second and last:     Additional wine information (rarely)
        // Last:                        The appellation name
        String[] nameParts = name.split("\\s*,\\s*");
        if (nameParts.length >= 1) {
            winery = nameParts[0];

            if (nameParts.length >= 2) {
                shortName = nameParts[1];
            } else {
                shortName = nameParts[0];
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Wine)) {
            return false;
        }

        Wine wine = (Wine) obj;
        return Objects.equals(wine.getId(), this.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static final DiffUtil.ItemCallback<Wine> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Wine>() {
                @Override
                public boolean areItemsTheSame(Wine oldItem, Wine newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }
                @Override
                public boolean areContentsTheSame(Wine oldItem, Wine newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
