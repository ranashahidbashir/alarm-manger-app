package com.alarm.manger.fragment;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.alarm.manger.R;
import com.alarm.manger.dialog.DialogTimePicker;
import com.alarm.manger.dialog.DialogTimePicker.onDateTimeListener;
import com.alarm.manger.util.AlarmInfo;
import com.alarm.manger.util.MyConstants;

/**
 * Created by Rana Shahid Bashir on 10/29/2016.
 */
public class FragmentAddAlarm extends Fragment implements onDateTimeListener {


    public onSubmitListener mListener;
    Button btnAdd, btnCancel;
    Button btnDate, btnTime;
    AlarmInfo alarmInfo;

    @Override
    public void setOnTimeListener(Message alarmTime) {


        Bundle bundle = alarmTime.getData();

        if (alarmInfo != null) {
            alarmInfo.setHour(bundle.getInt(MyConstants.HOUR));
            alarmInfo.setMinute(bundle.getInt(MyConstants.MINUTE));


            btnTime.setText(alarmInfo.getTime());
        }

    }

    @Override
    public void setOnDateListener(Message alarmDate) {

        Bundle bundle = alarmDate.getData();

        if (alarmInfo != null) {
            alarmInfo.setYear(bundle.getInt(MyConstants.YEAR));
            alarmInfo.setMonth(bundle.getInt(MyConstants.MONTH));
            alarmInfo.setDay(bundle.getInt(MyConstants.DAY));

            btnDate.setText(alarmInfo.getDate());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragmetn_add_alarm, container, false);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        iniActivityData();


    }

    public void iniActivityData() {


        View view = getView();

        if (getArguments().containsKey(MyConstants.NEW_ALARM))
            alarmInfo = (AlarmInfo) getArguments().getSerializable(MyConstants.NEW_ALARM);

        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnDate = (Button) view.findViewById(R.id.btn_date);
        btnTime = (Button) view.findViewById(R.id.btn_time);

        btnDate.setText(alarmInfo.getDate());
        btnTime.setText(alarmInfo.getTime());


        btnDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();

                bundle.putString(MyConstants.DIALOG_OPERATION, MyConstants.DATE_PICKER);
                bundle.putInt(MyConstants.YEAR, alarmInfo.getYear());
                bundle.putInt(MyConstants.MONTH, alarmInfo.getMonth());
                bundle.putInt(MyConstants.DAY, alarmInfo.getDay());


                startDateTimePickerDialog(bundle);


            }
        });

        btnTime.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();

                bundle.putString(MyConstants.DIALOG_OPERATION, MyConstants.TIME_PICKER);
                bundle.putInt(MyConstants.HOUR, alarmInfo.getHour());
                bundle.putInt(MyConstants.MINUTE, alarmInfo.getMinute());

                startDateTimePickerDialog(bundle);


            }
        });


        // et_last_name.setText(text);
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.setOnDoneListener(alarmInfo);
                getActivity().onBackPressed();

            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.setOnCancelListener("");
                getActivity().onBackPressed();

            }
        });
    }

    public void startDateTimePickerDialog(Bundle bundle) {


        DialogTimePicker fragment = new DialogTimePicker();

        fragment.mListener = FragmentAddAlarm.this;
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(fragment, MyConstants.DIALOG_DATE_TIME_PICKER);
        transaction.commit();
    }

    public interface onSubmitListener {

        void setOnDoneListener(AlarmInfo alarmInfo);

        void setOnCancelListener(String msg);
    }


}

