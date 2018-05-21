package com.mistong.timepickerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mistong.android.timepickerlibrary.picker.TimePicker;


public class MainActivity extends AppCompatActivity {
    String mHour = "00";
    String mMinute = "00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onTimePicker(View view) {
        final TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        picker.setSelectedItem(0, 0);
        picker.setTopLineVisible(false);

        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                showToast(hour + ":" + minute);
            }
        });

        picker.setOnWheelListener(new TimePicker.OnWheelListener() {
            @Override
            public void onHourWheeled(int index, String hour) {
                mHour = hour;
                if (hour.equals("00") && mMinute.equals("00")) {
                    picker.setRightBackgroundResource(R.drawable.submit_unselect_bg);
                    picker.setTheSelectTimeIsEffective(false);
                } else {
                    picker.setRightBackgroundResource(R.drawable.submit_bg);
                    picker.setTheSelectTimeIsEffective(true);
                }
            }

            @Override
            public void onMinuteWheeled(int index, String minute) {
                mMinute = minute;
                if (mHour.equals("00") && minute.equals("00")) {
                    picker.setRightBackgroundResource(R.drawable.submit_unselect_bg);
                    picker.setTheSelectTimeIsEffective(false);
                } else {
                    picker.setRightBackgroundResource(R.drawable.submit_bg);
                    picker.setTheSelectTimeIsEffective(true);
                }
            }
        });
        picker.setTheSelectTimeIsEffective(false);

        picker.setTextColor(getResources().getColor(R.color.color_333), getResources().getColor(R.color.color_ccc));
        picker.setLineColor(getResources().getColor(R.color.color_dedede));

        picker.setTopHeight(50);
        picker.setCancelText("自定义播放时长");
        picker.setLeftTextSize(16);
        picker.setLeftTextColor(getResources().getColor(R.color.color_434f59));
        picker.setCancelPressTextColor(getResources().getColor(R.color.color_434f59));

        picker.setSubmitText("完成");
        picker.setRightTextSize(14);
        picker.setRightTextColor(getResources().getColor(R.color.color_fff));
        picker.setRightBackgroundDrawable(getResources().getDrawable(R.drawable.submit_unselect_bg));
        picker.setRightWidth(58);
        picker.setRightHeight(27);
        picker.show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
