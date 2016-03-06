package com.example.loaders.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hyc on 16-3-7.
 */
public class MyContract {
    public static final String CONTENT_AUTHORITY = "com.example.loaders";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_CONTACTS = "contacts";


    public static class Tags implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACTS).build();

        public static final Uri CONTENT_FILTER_URI = Uri.withAppendedPath(
                CONTENT_URI, "filter");
        public static final String CONTENT_TYPE_ID = "tag";

        /**
         * Build {@link Uri} that references all tags.
         */
        public static Uri buildTagsUri() {
            return CONTENT_URI;
        }

        /**
         * Build a {@link Uri} that references a given tag.
         */
        public static Uri buildTagUri(String tagId) {
            return CONTENT_URI.buildUpon().appendPath(tagId).build();
        }

        /**
         */
        public static String getTagId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static class SearchTopicsSessions {

        static final String[] CONTACTS_PROJECTION=new String[]{
                BaseColumns._ID,
                "contact_name",
                "contact_number",
        };

        public static final String TOPIC_TAG_SELECTION =
                "contact_name " + " like ?";
    }
}
