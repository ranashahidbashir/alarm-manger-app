package com.alarm.manger.Adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alarm.manger.AlarmReceiver;
import com.alarm.manger.MainActivity;
import com.alarm.manger.R;
import com.alarm.manger.db.DatabaseHelper;
import com.alarm.manger.util.AlarmInfo;

import java.util.ArrayList;
import java.util.List;

public class AdapterAlarm extends ArrayAdapter<AlarmInfo> {


    LayoutInflater inflater;

    ArrayList<AlarmInfo> alarmInfos;
    Activity context;

    private DatabaseHelper db;


    public AdapterAlarm(Activity context, int resource,
                        List<AlarmInfo> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub

        this.context = context;


        this.alarmInfos = (ArrayList<AlarmInfo>) objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new DatabaseHelper(context);


    }

    @Override
    public int getCount() {

        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_alarm, null);

            holder.rlDateTime = (RelativeLayout) convertView
                    .findViewById(R.id.rl_date_time);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_time);
            holder.tvDate = (TextView) convertView
                    .findViewById(R.id.tv_date);
            holder.ivDelete = (ImageView) convertView
                    .findViewById(R.id.iv_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final AlarmInfo alarm = alarmInfos.get(position);

        if (alarmInfos != null) {

            holder.tvTime.setText(alarm.getTime());
            holder.tvDate.setText(alarm.getDate());

            holder.rlDateTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context instanceof MainActivity) {
                        ((MainActivity) context).callAddAlarmFragment(position, null);
                    }


                }
            });

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.dialog_del_alarm_label))
                            .setMessage(context.getResources().getString(R.string.dialog_del_alarm_msg))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    db.deleteAlarm(alarm.getId());
                                    cancelAlarm(alarm.getId());
                                    alarmInfos.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

        }

        return convertView;
    }

    public void cancelAlarm(int id) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if ((alarmManager != null) && (pendingIntent != null)) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }


    private class ViewHolder {
        public RelativeLayout rlDateTime;
        TextView tvTime, tvDate;
        ImageView ivDelete;

    }

}
