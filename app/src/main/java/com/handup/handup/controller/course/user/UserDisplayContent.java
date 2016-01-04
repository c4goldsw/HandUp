package com.handup.handup.controller.course.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to show each user in a course in the course activity.  Note, this is wizard-
 * generated code - I'm simply changing out parts of that activity to suit my own needs
 */
public class UserDisplayContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<UserDisplayItem> ITEMS = new ArrayList<UserDisplayItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, UserDisplayItem> ITEM_MAP = new HashMap<String, UserDisplayItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createUserDispalyItem(i));
        }
    }

    private static void addItem(UserDisplayItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static UserDisplayItem createUserDispalyItem(int position) {
        return new UserDisplayItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class UserDisplayItem {
        public final String id;
        public final String content;
        public final String details;

        public UserDisplayItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
