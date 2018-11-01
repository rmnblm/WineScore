package ch.hsr.winescore.model;

public class Favorite {

    private String userId;
    private String wineId;

    public Favorite() {
    }

    public Favorite(String userId, String wineId) {
        this.userId = userId;
        this.wineId = wineId;
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
}
