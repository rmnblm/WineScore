package ch.hsr.winescore.domain.models;

import com.google.gson.annotations.SerializedName;

public enum WineType {
    @SerializedName("sweet")
    SWEET,
    @SerializedName("sparkling")
    SPARKLING,
    @SerializedName("dry")
    DRY,
    @SerializedName("")
    NONE
}
