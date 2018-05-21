package com.mistong.android.timepickerlibrary.picker;

import android.app.Activity;

/**
 * 时间选择器
 */
public class TimePicker extends HoursTimePicker {

    /**
     * @see #HOUR_24
     * @see #HOUR_12
     */
    public TimePicker(Activity activity, @TimeMode int mode) {
        super(activity, mode);
    }

    /**
     * 设置时间显示的单位
     */
    @Deprecated
    @Override
    public final void setLabel(String hourLabel, String minuteLabel) {
        super.setLabel(hourLabel, minuteLabel);
    }


    /**
     * @deprecated use {@link #setRangeStart(int, int)} instead
     */
    @Deprecated
    @Override
    public void setTimeRangeStart(int startHour, int startMinute) {
        super.setTimeRangeStart(startHour, startMinute);
    }

    /**
     * @deprecated use {@link #setRangeEnd(int, int)} instead
     */
    @Deprecated
    @Override
    public void setTimeRangeEnd(int endHour, int endMinute) {
        super.setTimeRangeEnd(endHour, endMinute);
    }

    /**
     * @deprecated use setRangeStart and setRangeEnd instead
     */
    @Deprecated
    public void setRange(int startHour, int endHour) {
        super.setTimeRangeStart(startHour, 0);
        super.setTimeRangeEnd(endHour, 59);
    }


    /**
     * 设置范围：开始的时分
     */
    public void setRangeStart(int startHour, int startMinute) {
        super.setTimeRangeStart(startHour, startMinute);
    }

    /**
     * 设置范围：结束的时分
     */
    public void setRangeEnd(int endHour, int endMinute) {
        super.setTimeRangeEnd(endHour, endMinute);
    }

    /**
     * 设置默认选中的时间
     */
    @Deprecated
    @Override
    public final void setSelectedItem(int hour, int minute) {
        super.setSelectedItem(hour, minute);
    }


    /**
     * @deprecated use {@link #setOnWheelListener(OnWheelListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnWheelListener(HoursTimePicker.OnWheelListener onWheelListener) {
        super.setOnWheelListener(onWheelListener);
    }

    /**
     * 设置滑动监听器
     */
    public void setOnWheelListener(final OnWheelListener listener) {
        if (null == listener) {
            return;
        }
        super.setOnWheelListener(new HoursTimePicker.OnWheelListener() {
            @Override
            public void onHourWheeled(int index, String hour) {
                listener.onHourWheeled(index, hour);
            }

            @Override
            public void onMinuteWheeled(int index, String minute) {
                listener.onMinuteWheeled(index, minute);
            }
        });
    }

    /**
     * @deprecated use {@link #setOnTimePickListener(OnTimePickListener)} instead
     */
    @Deprecated
    @Override
    public final void setOnHoursTimePickListener(OnHoursTimePickListener listener) {
        super.setOnHoursTimePickListener(listener);
    }

    public void setOnTimePickListener(final OnTimePickListener listener) {
        if (null == listener) {
            return;
        }
        super.setOnHoursTimePickListener(new HoursTimePicker.OnTimePickListener() {
            @Override
            public void onHoursTimePicked(String hour, String minute) {
                listener.onTimePicked(hour, minute);
            }
        });
    }

    public interface OnTimePickListener {

        void onTimePicked(String hour, String minute);

    }

    public interface OnWheelListener {

        void onHourWheeled(int index, String hour);

        void onMinuteWheeled(int index, String minute);

    }

}
