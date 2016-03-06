package com.atrue.hyc.searchview.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HYC on 2016/2/24.
 * 所有 sqlite 数据库的 表名  和
 */
public class ScheduleConract {
    public static final String CONTENT_TYPE_APP_BASE="hyc.";
    public static final String CONTENT_TYPE_BASE="vnd.android.cursor.dir/vnd."+CONTENT_TYPE_APP_BASE;
    public static final String CONTENT_ITEN_TYPE_BASE="vnd.android.cursor.item/vnd."+CONTENT_TYPE_APP_BASE;


    interface ContactColmns{
        String CONTACT_NAME="contact_name";
        String CONTACT_ID="contact_id";
        String CONTACT_NUMBER="contact_number";

    }
    public static final String CONTENT_AUTHORITY="com.atrue.hyc.searchview";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_SESSIONS = "sessions";
    private static final String PATH_CONTACTS = "contacts";

    public static String makeContentItemType(String id){
        if (id!=null){
            return CONTENT_TYPE_BASE+id;
        }else {
            return null;
        }
    }


    public static class Contacts implements ContactColmns,BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACTS).build();

        /**
         * Build {@link Uri} for requested {@link #CONTACT_ID}
         */
        public static Uri buildContactUri(String contactId){
            return CONTENT_URI.buildUpon().appendPath(contactId).build();
        }




        public static  String getContactId(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

    public static class Sessions implements BaseColumns,ContactColmns{

        public static final String QUERY_PARAMETER_TAG_FILTER="filter";
        public static final String QUERY_PARAMETER_CATEGORIER="categories";

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_SESSIONS).build();

        public static final String CONTENT_TYPE_ID = "session";


    }

}
