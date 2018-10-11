package ch.hsr.winescore.model;

import com.google.gson.annotations.SerializedName;

public enum WineType {
    @SerializedName("sweet")
    Sweet,
    @SerializedName("sparkling")
    Sparkling,
    @SerializedName("dry")
    Dry,
    @SerializedName("")
    None
}
