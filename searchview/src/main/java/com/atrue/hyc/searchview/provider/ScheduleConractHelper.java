package com.atrue.hyc.searchview.provider;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by HYC on 2016/2/26.
 */
public class ScheduleConractHelper {
    public static final String QUERY_PARAMETER_DISTINCT = "distinct";
    private static final String QUERY_PARAMETER_CALLER_IS_SYNC_ADAPTER = "callerIsSyncAdapter";


    /**
     * 具体含义 : {@see <a href="http://www.runoob.com/sqlite/sqlite-distinct-keyword.html"> SQLite Distinct 关键字</a> }
     * SQLite 的 DISTINCT 关键字与 SELECT 语句一起使用，来消除所有重复的记录，并只获取唯一一次记录。
     * 有可能出现一种情况，在一个表中有多个重复的记录。
     * 当提取这样的记录时，DISTINCT 关键字就显得特别有意义，它只获取唯一一次记录，而不是获取重复记录。
     */
    public static boolean isQueryDistinct(Uri uri) {
        return !TextUtils.isEmpty(uri.getQueryParameter(QUERY_PARAMETER_DISTINCT));
    }

    public static boolean isUriCalledFromSyncAdapter(Uri uri) {
        return uri.getBooleanQueryParameter(QUERY_PARAMETER_CALLER_IS_SYNC_ADAPTER, false);
    }
}
