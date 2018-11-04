package ch.hsr.winescore.domain.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FavoriteTest {

    @Test
    public void whenCallingConstructorWithParams_itSetsProperties() {
        Favorite favorite = new Favorite("userId", "wineId");
        assertEquals("userId", favorite.getUserId());
        assertEquals("wineId", favorite.getWineId());
    }
    @Test
    public void whenSettingProperty_itSetsProperty() {
        Favorite favorite = new Favorite();

        favorite.setUserId("userId");
        assertEquals("userId", favorite.getUserId());

        favorite.setWineId("wineId");
        assertEquals("wineId", favorite.getWineId());
    }
}
