package com.example.autoalarm.app.Alarm;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jonas on 2014-05-19.
 */
public class CalendarEvents {
    Context activity;
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public CalendarEvents(Context activity){
        this.activity = activity;
    }
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Events._ID,                           // 0
            CalendarContract.Events.ACCOUNT_NAME,                  // 1
            CalendarContract.Events.TITLE,                         // 2
            CalendarContract.Events.DTSTART                        // 3
    };

    public ArrayList<java.sql.Timestamp> getFirstDayEvents(int days){
        ArrayList<java.sql.Timestamp> dates = new ArrayList<java.sql.Timestamp>(days);
        // Run query
        Cursor cur = null;
        ContentResolver cr = activity.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Events.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Events.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"jonas.stonevalley@gmail.com", "com.google",
                "jonas.stonevalley@gmail.com.com"};
        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        java.sql.Timestamp dtstart;
        java.sql.Timestamp prevStart = new java.sql.Timestamp(System.currentTimeMillis());
        while(!cur.isLast()){
            cur.moveToNext();
            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            dtstart = java.sql.Timestamp.valueOf(cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART)));
            if (dtstart.after(prevStart)) {
                dates.add(dtstart);
                
            }
        }
        return dates;
    }

}
