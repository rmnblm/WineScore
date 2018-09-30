package ch.hsr.winescore.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Title " + position, "Subtitle " + position, (position * 10) % 100, ((char) (65 + position % 3)) + "+");
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String title;
        public final String subtitle;
        public final double score;
        public final String index;
        public final int vintage = 1994;
        public final String color = "red";
        public final String appellation = "Pomerol";
        public final String regions = "Bordeaux";
        public final String country = "France";


        public DummyItem(String id, String title, String subtitle, int score, String index) {
            this.title = title;
            this.subtitle = subtitle;
            this.score = score;
            this.index = index;
            this.id = id;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
