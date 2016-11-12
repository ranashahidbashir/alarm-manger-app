package com.alarm.manger.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alarm.manger.util.AlarmInfo;
import com.alarm.manger.util.DateUtil;
import com.alarm.manger.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rana Shahid Bashir on 10/28/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "alarm-manger_db";

    // Table Names
    private static final String TABLE_ALARM = "tb_alarm";

    // Common column names
    private static final String KEY_ID = "id";

    private static final String KEY_ALARM_INFO = "alarm_info";

    private static final String KEY_CREATED_AT = "c_date";

    private static final String KEY_UPDATED_AT = "u_date";

    private static final String KEY_STATUS = "status";


    // Tag table create statement
    private static final String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_ALARM
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ALARM_INFO + " TEXT,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_UPDATED_AT + " DATETIME"


            + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ALARM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        // create new tables
        onCreate(db);
    }


    /*
 * Creating Alarm
 */
    public long createAlarm(AlarmInfo alarmInfo) {


        SQLiteDatabase db = this.getWritableDatabase();

        long tag_id = -1;

        if (!alarmAlreadyExist(alarmInfo.getId())) {

            ContentValues values = new ContentValues();

            values.put(KEY_ID, alarmInfo.getId());
            values.put(KEY_ALARM_INFO, JsonUtil.convertAlarmObjectIntoJson(alarmInfo));
            values.put(KEY_CREATED_AT, DateUtil.getDateTime());
            values.put(KEY_UPDATED_AT, DateUtil.getDateTime());

            // insert row
            tag_id = db.insert(TABLE_ALARM, null, values);
        } else {
            //Quiz already exist update quiz
            updateAlarm(alarmInfo);
        }

        return tag_id;
    }


    public void updateAlarm(AlarmInfo alarmInfo) {

        SQLiteDatabase db = this.getWritableDatabase();


        String updateQuery = "UPDATE  " + TABLE_ALARM + " SET  " +
                KEY_ALARM_INFO + " = '" + JsonUtil.convertAlarmObjectIntoJson(alarmInfo) + "'," +
                KEY_UPDATED_AT + " = '" + DateUtil.getDateTime() + "' " +
                " where " + KEY_ID + "=" + alarmInfo.getId();

        Log.d(LOG, updateQuery);

        db.execSQL(updateQuery);


    }


    public boolean alarmAlreadyExist(int alarm_id) {


        // List<Quiz> todos = new ArrayList<Quiz>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM + " where " + KEY_ID + "=" + (alarm_id);

        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        int count = c.getCount();
        if (count > 0) {
            return true;
        }
        return false;

    }


    /*
 * getting all category
 * */
    public List<AlarmInfo> getAllAlarm() {
        List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM;

        Log.d(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        AlarmInfo alarmInfo;
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                alarmInfo = new AlarmInfo();


                alarmInfo = JsonUtil.convertJsonToAlarmObject(c.getString((c.getColumnIndex(KEY_ALARM_INFO))));


                alarmInfoList.add(alarmInfo);
            } while (c.moveToNext());
        }

        return alarmInfoList;
    }


    /*
     * delete alarm
	 */
    public void deleteAlarm(int id) {


        SQLiteDatabase db = this.getWritableDatabase();


        String deleteQuery = "delete from " + TABLE_ALARM +

                " where " + KEY_ID + "=" + id;

        Log.d(LOG, deleteQuery);

        db.execSQL(deleteQuery);


    }


}
