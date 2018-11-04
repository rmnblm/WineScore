package ch.hsr.winescore.domain.models;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CommentTest {

    @Test
    public void whenCallingConstructorWithParams_itSetsProperties() {
        Comment comment = new Comment("userId", "username", "wineId", "content");
        assertEquals("userId", comment.getUserId());
        assertEquals("username", comment.getUserName());
        assertEquals("wineId", comment.getWineId());
        assertEquals("content", comment.getContent());
        assertNull(comment.getTimestamp());
    }
    @Test
    public void whenSettingProperty_itSetsProperty() {
        Comment comment = new Comment();

        comment.setUserId("userId");
        assertEquals("userId", comment.getUserId());

        comment.setUserName("username");
        assertEquals("username", comment.getUserName());

        comment.setWineId("wineId");
        assertEquals("wineId", comment.getWineId());

        comment.setContent("content");
        assertEquals("content", comment.getContent());

        Date date = new Date();
        com.google.firebase.Timestamp timestamp = new com.google.firebase.Timestamp(date);
        comment.setTimestamp(timestamp);
        assertEquals(timestamp, comment.getTimestamp());
    }
}
