package ch.hsr.winescore.model;

public class Rating {

    private String userId;
    private String wineId;
    private Integer ratingValue;

    public Rating() {
    }

    public Rating(String userId, String wineId, Integer ratingValue) {
        this.userId = userId;
        this.wineId = wineId;
        this.ratingValue = ratingValue;
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

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }
}
