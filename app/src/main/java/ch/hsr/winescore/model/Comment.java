package ch.hsr.winescore.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Comment {

    private String userId;
    private String userName;
    private String wineId;
    private String content;
    @ServerTimestamp
    private Timestamp timestamp = null;

    public Comment() {
    }

    public Comment(String userId, String userName, String wineId, String content) {
        this.userId = userId;
        this.userName = userName;
        this.wineId = wineId;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWineId() {
        return wineId;
    }

    public void setWineId(String wineId) {
        this.wineId = wineId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
