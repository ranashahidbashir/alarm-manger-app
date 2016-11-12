package com.alarm.manger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alarm.manger.Adapter.AdapterAlarm;
import com.alarm.manger.db.DatabaseHelper;
import com.alarm.manger.fragment.FragmentAddAlarm;
import com.alarm.manger.fragment.FragmentAddAlarm.onSubmitListener;
import com.alarm.manger.util.AlarmInfo;
import com.alarm.manger.util.MyConstants;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends FragmentActivity implements onSubmitListener {


    String TAG = "MainActivity";
    ListView lvAlarm;
    AdapterAlarm adapter;
    ArrayList<AlarmInfo> alarmInfos;
    int editId = -1;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniComponent();
        iniData();

    }


    /*
    * initialize activity components
    *
    * */
    private void iniComponent() {


        lvAlarm = (ListView) findViewById(R.id.lv_alarm);

        db = new DatabaseHelper(this);


    }

    public void iniData() {

        alarmInfos = (ArrayList<AlarmInfo>) db.getAllAlarm();

        adapter = new AdapterAlarm(this, R.layout.row_alarm, alarmInfos);

        lvAlarm.setAdapter(adapter);

    }


    public void addAlarm(View v) {

        Log.d(TAG, "0NAddNewAlarm");

        editId = -1;

        callAddAlarmFragment(-1, getNewAlarmInstance());


    }


    public AlarmInfo getNewAlarmInstance() {

        Calendar c = Calendar.getInstance();
        AlarmInfo alarmInfo = new AlarmInfo();

        if ((alarmInfos != null) && (alarmInfos.size() > 0)) {

            alarmInfo.setId((alarmInfos.get(alarmInfos.size() - 1).getId()) + 1);
        } else {
            alarmInfo.setId(1);
        }

        alarmInfo.setYear(c.get(Calendar.YEAR));
        alarmInfo.setMonth(c.get(Calendar.MONTH));
        alarmInfo.setDay(c.get(Calendar.DATE));

        alarmInfo.setHour(c.get(Calendar.HOUR_OF_DAY));
        alarmInfo.setMinute(c.get(Calendar.MINUTE));
        alarmInfo.setSec(0);
        alarmInfo.setStatus("1");

        return alarmInfo;


    }


    @Override
    public void setOnDoneListener(AlarmInfo alarmInfo) {

        Log.d(TAG, "OnDoneAlarm");
        if (alarmInfo != null)
            iniNewAlarm(alarmInfo);

    }


    @Override
    public void setOnCancelListener(String msg) {

        Log.d(TAG, "onCancel");
        editId = -1;
    }


    public void iniNewAlarm(AlarmInfo alarm_info) {


        Calendar calendar = Calendar.getInstance();
        Calendar currTime = Calendar.getInstance();

        /*
        *ini alarm date & time
        *
        * */
        calendar.set(Calendar.YEAR, alarm_info.getYear());
        calendar.set(Calendar.MONTH, alarm_info.getMonth());
        calendar.set(Calendar.DATE, alarm_info.getDay());

        calendar.set(Calendar.HOUR_OF_DAY, alarm_info.getHour());
        calendar.set(Calendar.MINUTE, alarm_info.getMinute());
        calendar.set(Calendar.SECOND, alarm_info.getSec());


        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReceiver in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class

        AlarmManager alarmManager;


        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(MyConstants.ALARM_ID, alarm_info.getId());

        // save object for further use.
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.NEW_ALARM, alarm_info);
        intent.putExtras(bundle);


        PendingIntent pi = PendingIntent.getBroadcast(this, alarm_info.getId(), intent, 0);
        /*alarmManager[f]*/
        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);


        Long mtime = calendar.getTimeInMillis();
        if (currTime.getTimeInMillis() < mtime) {
            /*alarmManager[f]*/

            //set the alarm for particular time
            alarmManager.set(AlarmManager.RTC_WAKEUP, mtime, pi);
            db.createAlarm(alarm_info);

            refreshAdaptor(alarm_info);
        } else
            Toast.makeText(getApplication(), getResources().getString(R.string.msg_wrong_date_time), Toast.LENGTH_SHORT).show();
    }


    public void refreshAdaptor(AlarmInfo alarm_info) {

        if (editId > -1) {
            alarmInfos.set(editId, alarm_info);
        } else
            alarmInfos.add(alarm_info);
        editId = -1;

        if (adapter != null)
            adapter.notifyDataSetChanged();


    }


    public void callAddAlarmFragment(int pos, AlarmInfo alarmInfo) {

        if (pos > -1) {
            alarmInfo = alarmInfos.get(pos);
            editId = pos;
        }
        if (alarmInfo != null) {
            FragmentAddAlarm addAlarm = new FragmentAddAlarm();
            addAlarm.mListener = MainActivity.this;

            Bundle bundle = new Bundle();

            if (alarmInfo != null)
                bundle.putSerializable(MyConstants.NEW_ALARM, alarmInfo);

            addAlarm.setArguments(bundle);

            findViewById(R.id.fl_add_alarm).setVisibility(View.VISIBLE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.addToBackStack(MainActivity.class.getName());
            ft.replace(R.id.fl_add_alarm, addAlarm);
            ft.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
