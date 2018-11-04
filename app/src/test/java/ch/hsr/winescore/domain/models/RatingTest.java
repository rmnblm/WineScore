package ch.hsr.winescore.domain.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RatingTest {

    @Test
    public void whenCallingConstructorWithParams_itSetsProperties() {
        Rating rating = new Rating("userId", "wineId", 5);
        assertEquals("userId", rating.getUserId());
        assertEquals("wineId", rating.getWineId());
        assertEquals(5, (long) rating.getRatingValue());
    }

    @Test
    public void whenSettingProperty_itSetsProperty() {
        Rating rating = new Rating();

        rating.setUserId("userId");
        assertEquals("userId", rating.getUserId());

        rating.setWineId("wineId");
        assertEquals("wineId", rating.getWineId());

        rating.setRatingValue(5);
        assertEquals(5, (long) rating.getRatingValue());
    }
}
