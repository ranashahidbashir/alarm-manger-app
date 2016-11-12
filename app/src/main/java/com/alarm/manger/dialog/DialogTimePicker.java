package com.alarm.manger.dialog;


import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Message;


import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.alarm.manger.util.MyConstants;



/**
 * Created by Rana Shahid Bashir on 10/28/2016.
 */
public class DialogTimePicker extends DialogFragment {

    private int timeYear;
    private int timeMonth;
    private int timeDay;
    private int timeHour;
    private int timeMinute;


    public onDateTimeListener mListener;


    public interface onDateTimeListener {
        void setOnTimeListener(Message alarmTime);
        void setOnDateListener(Message alarmTime);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeHour = hourOfDay;
                timeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(MyConstants.HOUR, timeHour);
                b.putInt(MyConstants.MINUTE, timeMinute);
                Message msg = new Message();
                msg.setData(b);

                mListener.setOnTimeListener(msg);

            }
        };


        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Bundle b = new Bundle();
                b.putInt(MyConstants.YEAR, year);
                b.putInt(MyConstants.MONTH, monthOfYear);
                b.putInt(MyConstants.DAY, dayOfMonth);

                Message msg = new Message();
                msg.setData(b);
                mListener.setOnDateListener(msg);
            }


        };

        if(bundle.getString(MyConstants.DIALOG_OPERATION).equalsIgnoreCase(MyConstants.TIME_PICKER) ) {

            timeHour = bundle.getInt(MyConstants.HOUR);
            timeMinute = bundle.getInt(MyConstants.MINUTE);

            return new TimePickerDialog(getActivity(), timeListener, timeHour, timeMinute, false);
        }
        else {
            timeYear = bundle.getInt(MyConstants.YEAR);
            timeMonth = bundle.getInt(MyConstants.MONTH);
            timeDay = bundle.getInt(MyConstants.DAY);
            return  new DatePickerDialog(getActivity() , dateListener ,timeYear , timeMonth , timeDay);

        }


    }
}
