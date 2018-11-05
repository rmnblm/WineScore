package ch.hsr.winescore.domain.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WineTest {

    @Test
    public void whenCallingConstructorWithParams_itSetsProperties() {
        Wine wine = new Wine("name", "wineId");
        assertEquals("name", wine.getName());
        assertEquals("wineId", wine.getWineId());
        assertNotNull(wine.getRegions());
        assertEquals(0, wine.getRegions().size());
    }

    @Test
    public void whenNameConsistsOfOnePart_itSetsNameAsWineryAndShortname() {
        final String name = "Domaine Auguste Clape";
        Wine wine = new Wine(name, "wineId");
        assertEquals(name, wine.getWinery());
        assertEquals(name, wine.getShortName());
        assertEquals(name, wine.getName());
    }

    @Test
    public void whenNameConsistsOfTwoParts_itSetsFirstPartAsWineryAndShortname() {
        final String name = "Domaine Auguste Clape, Cornas";
        Wine wine = new Wine(name, "wineId");
        assertEquals("Domaine Auguste Clape", wine.getWinery());
        assertEquals("Domaine Auguste Clape", wine.getShortName());
        assertEquals(name, wine.getName());
    }

    @Test
    public void whenNameConsistsOfThreeParts_itSetsFirstPartAsWineryAndSecondPartAsShortname() {
        final String name = "Louis Roederer, Cristal Brut Millesime, Champagne";
        Wine wine = new Wine(name, "wineId");
        assertEquals("Louis Roederer", wine.getWinery());
        assertEquals("Cristal Brut Millesime", wine.getShortName());
        assertEquals(name, wine.getName());
    }
}
