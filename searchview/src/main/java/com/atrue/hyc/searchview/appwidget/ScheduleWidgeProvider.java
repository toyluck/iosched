package com.atrue.hyc.searchview.appwidget;

import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import static com.atrue.hyc.searchview.util.LogUtils.LOGD;
import static com.atrue.hyc.searchview.util.LogUtils.makeLogTag;

/**
 * Created by HYC on 2016/2/26.
 */
public class ScheduleWidgeProvider extends AppWidgetProvider {
    private static final String Tag = makeLogTag(ScheduleWidgeProvider.class);
    private static final String REFRESH_ACTION =
            "com.atrue.hyc.searchview.appwidget.action.REFRESH";
    private static final String EXTRA_PERFORM_SYNC =
            "com.atrue.hyc.searchview.appwidget.extra.PERFORM_SYNC";

    /**
     * todo
     *
     * @param context
     * @param performSync
     * @return
     */
    public static Intent getRefreshBroadcastIntent(Context context, boolean performSync) {

        return new Intent(REFRESH_ACTION).setComponent(new ComponentName(context, ScheduleWidgeProvider.class)).putExtra(EXTRA_PERFORM_SYNC, performSync);
    }

    @Override
    public void onReceive(Context context, Intent widgetIntent) {
        String action = widgetIntent.getAction();

        if (REFRESH_ACTION.equals(action)){
            LOGD(Tag,"received REFRESH ACTION from widget");
            boolean shouldSync = widgetIntent.getBooleanExtra(EXTRA_PERFORM_SYNC, false);


        }

        super.onReceive(context, widgetIntent);
    }
}
