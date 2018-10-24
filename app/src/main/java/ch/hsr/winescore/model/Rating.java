package ch.hsr.winescore.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Rating {

    private String userId;
    private String wineId;
    private Integer rating;

    public Rating() {
    }

    public Rating(String userId, String wineId, Integer rating) {
        this.userId = userId;
        this.wineId = wineId;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWineId() {
        return wineId;
    }

    public void setWineId(String wineId) {
        this.wineId = wineId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
