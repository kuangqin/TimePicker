package com.mistong.android.timepickerlibrary.picker;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mistong.android.timepickerlibrary.R;
import com.mistong.android.timepickerlibrary.utils.ConvertUtils;
import com.mistong.android.timepickerlibrary.utils.DateUtils;
import com.mistong.android.timepickerlibrary.utils.LogUtils;
import com.mistong.android.timepickerlibrary.widget.WheelView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


/**
 * 日期时间选择器，可同时选中日期及时间
 */
public class HoursTimePicker extends WheelPicker {
    /**
     * 不显示
     */
    public static final int NONE = -1;
    /**
     * 24小时
     */
    public static final int HOUR_24 = 3;
    /**
     * @deprecated use {@link #HOUR_24} instead
     */
    @Deprecated
    public static final int HOUR_OF_DAY = 3;
    /**
     * 12小时
     */
    public static final int HOUR_12 = 4;
    /**
     * @deprecated use {@link #HOUR_12} instead
     */
    @Deprecated
    public static final int HOUR = 4;

    private ArrayList<String> hours = new ArrayList<>();
    private ArrayList<String> minutes = new ArrayList<>();
    private String hourLabel = "小时", minuteLabel = "分钟";
    private String selectedHour = "", selectedMinute = "";
    private OnWheelListener onWheelListener;
    private OnHoursTimePickListener onHoursTimePickListener;
    private int timeMode = HOUR_24;
    private int startHour, startMinute = 0;
    private int endHour, endMinute = 59;
    private int textSize = WheelView.TEXT_SIZE;
    private int numtextSize = WheelView.NUM_TEXT_SIZE;
    private boolean resetWhileWheel = true;

    @IntDef(value = {NONE, HOUR_24, HOUR_12})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeMode {
    }


    public HoursTimePicker(Activity activity, @TimeMode int timeMode) {
        super(activity);
        if (timeMode == NONE) {
            throw new IllegalArgumentException("The modes are NONE at the same time");
        }
        if (timeMode != NONE) {
            if (screenWidthPixels < 720) {
                textSize = 24;//年月日时分，比较宽，设置字体小一点才能显示完整
            } else if (screenWidthPixels < 480) {
                textSize = 42;
            }
        }
        //根据时间模式初始化小时范围
        if (timeMode == HOUR_12) {
            startHour = 1;
            endHour = 12;
        } else {
            startHour = 0;
            endHour = 23;
        }
        this.timeMode = timeMode;
    }

    /**
     * 滚动时是否重置下一级的索引
     */
    public void setResetWhileWheel(boolean resetWhileWheel) {
        this.resetWhileWheel = resetWhileWheel;
    }

    /**
     * 设置范围：开始的时分
     */
    public void setTimeRangeStart(int startHour, int startMinute) {
        if (timeMode == NONE) {
            throw new IllegalArgumentException("Time mode invalid");
        }
        boolean illegal = false;
        if (startHour < 0 || startMinute < 0 || startMinute > 59) {
            illegal = true;
        }
        if (timeMode == HOUR_12 && (startHour == 0 || startHour > 12)) {
            illegal = true;
        }
        if (timeMode == HOUR_24 && startHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.startHour = startHour;
        this.startMinute = startMinute;
        initHourData();
    }

    /**
     * 设置范围：结束的时分
     */
    public void setTimeRangeEnd(int endHour, int endMinute) {
        if (timeMode == NONE) {
            throw new IllegalArgumentException("Time mode invalid");
        }
        boolean illegal = false;
        if (endHour < 0 || endMinute < 0 || endMinute > 59) {
            illegal = true;
        }
        if (timeMode == HOUR_12 && (endHour == 0 || endHour > 12)) {
            illegal = true;
        }
        if (timeMode == HOUR_24 && endHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.endHour = endHour;
        this.endMinute = endMinute;
        initHourData();
    }

    /**
     * 设置时分的显示单位
     */
    public void setLabel(String hourLabel, String minuteLabel) {
        this.hourLabel = hourLabel;
        this.minuteLabel = minuteLabel;
    }

    /**
     * 设置默认选中的时分
     */
    public void setSelectedItem(int hour, int minute) {
        LogUtils.verbose(this, "change months and days while set selected");
        if (timeMode != NONE) {
            selectedHour = DateUtils.fillZero(hour);
            selectedMinute = DateUtils.fillZero(minute);
        }
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnHoursTimePickListener(OnHoursTimePickListener listener) {
        this.onHoursTimePickListener = listener;
    }

    public String getSelectedHour() {
        if (timeMode != NONE) {
            return selectedHour;
        }
        return "";
    }

    public String getSelectedMinute() {
        if (timeMode != NONE) {
            return selectedMinute;
        }
        return "";
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        // 如果未设置默认项，则需要在此初始化数据
        if (timeMode != NONE && hours.size() == 0) {
            LogUtils.verbose(this, "init hours before make view");
            initHourData();
        }
        if (timeMode != NONE && minutes.size() == 0) {
            LogUtils.verbose(this, "init minutes before make view");
            changeMinuteData(DateUtils.trimZero(selectedHour));
        }

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView hourView = createWheelView();
        final WheelView minuteView = createWheelView();

        if (timeMode != NONE) {
            final LinearLayout layouthour = new LinearLayout(activity);
            layouthour.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
            layouthour.setOrientation(LinearLayout.HORIZONTAL);
            layouthour.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams paramTest = (LinearLayout.LayoutParams) layouthour.getLayoutParams();
            paramTest.rightMargin = ConvertUtils.toPx(getContext(), 50);
            layouthour.setLayoutParams(paramTest);


            hourView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            hourView.setItems(hours, selectedHour);
            hourView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedHour = hours.get(index);
                    if (onWheelListener != null) {
                        onWheelListener.onHourWheeled(index, selectedHour);
                    }
                    LogUtils.verbose(this, "change minutes after hour wheeled");
                    changeMinuteData(DateUtils.trimZero(selectedHour));
                    minuteView.setItems(minutes, selectedMinute);
                }
            });
            layouthour.addView(hourView);

            if (!TextUtils.isEmpty(hourLabel)) {
                TextView labelView = createLabelView();
                labelView.setTextSize(textSize);
                labelView.setText(hourLabel);
                labelView.setTextColor(getContext().getResources().getColor(R.color.color_333));
                layouthour.addView(labelView);
            }
            layout.addView(layouthour);


            LinearLayout layoutminute = new LinearLayout(activity);
            layoutminute.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
            layoutminute.setOrientation(LinearLayout.HORIZONTAL);
            layoutminute.setGravity(Gravity.CENTER_VERTICAL);

            minuteView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            minuteView.setItems(minutes, selectedMinute);
            minuteView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
                @Override
                public void onSelected(int index) {
                    selectedMinute = minutes.get(index);
                    if (onWheelListener != null) {
                        onWheelListener.onMinuteWheeled(index, selectedMinute);
                    }
                }
            });
            layoutminute.addView(minuteView);
            if (!TextUtils.isEmpty(minuteLabel)) {
                TextView labelView = createLabelView();
                labelView.setTextSize(textSize);
                labelView.setText(minuteLabel);
                labelView.setTextColor(getContext().getResources().getColor(R.color.color_333));
                layoutminute.addView(labelView);
            }

            layout.addView(layoutminute);
        }
        return layout;
    }

    @Override
    protected void onSubmit() {
        if (onHoursTimePickListener == null) {
            return;
        }
        String hour = getSelectedHour();
        String minute = getSelectedMinute();
        ((OnTimePickListener) onHoursTimePickListener).onHoursTimePicked(hour, minute);
    }

    private int findItemIndex(ArrayList<String> items, int item) {
        //折半查找有序元素的索引
        int index = Collections.binarySearch(items, item, new Comparator<Object>() {
            @Override
            public int compare(Object lhs, Object rhs) {
                String lhsStr = lhs.toString();
                String rhsStr = rhs.toString();
                lhsStr = lhsStr.startsWith("0") ? lhsStr.substring(1) : lhsStr;
                rhsStr = rhsStr.startsWith("0") ? rhsStr.substring(1) : rhsStr;
                try {
                    return Integer.parseInt(lhsStr) - Integer.parseInt(rhsStr);
                } catch (java.lang.NumberFormatException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
        if (index < 0) {
            throw new IllegalArgumentException("Item[" + item + "] out of range");
        }
        return index;
    }

    private void initHourData() {
        hours.clear();
        int currentHour = 0;
        if (!resetWhileWheel) {
            if (timeMode == HOUR_24) {
                currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            } else {
                currentHour = Calendar.getInstance().get(Calendar.HOUR);
            }
        }
        for (int i = startHour; i <= endHour; i++) {
            String hour = DateUtils.fillZero(i);
            if (!resetWhileWheel) {
                if (i == currentHour) {
                    selectedHour = hour;
                }
            }
            hours.add(hour);
        }
        if (hours.indexOf(selectedHour) == -1) {
            //当前设置的小时不在指定范围，则默认选中范围开始的小时
            selectedHour = hours.get(0);
        }
        if (!resetWhileWheel) {
            selectedMinute = DateUtils.fillZero(Calendar.getInstance().get(Calendar.MINUTE));
        }
    }

    private void changeMinuteData(int selectedHour) {
        minutes.clear();
        if (startHour == endHour) {
            if (startMinute > endMinute) {
                int temp = startMinute;
                startMinute = endMinute;
                endMinute = temp;
            }
            for (int i = startMinute; i <= endMinute; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        } else if (selectedHour == startHour) {
            for (int i = startMinute; i <= 59; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        } else if (selectedHour == endHour) {
            for (int i = 0; i <= endMinute; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        } else {
            for (int i = 0; i <= 59; i++) {
                minutes.add(DateUtils.fillZero(i));
            }
        }
        if (minutes.indexOf(selectedMinute) == -1) {
            //当前设置的分钟不在指定范围，则默认选中范围开始的分钟
            selectedMinute = minutes.get(0);
        }
    }

    public interface OnWheelListener {
        void onHourWheeled(int index, String hour);

        void onMinuteWheeled(int index, String minute);

    }

    protected interface OnHoursTimePickListener {

    }

    public interface OnTimePickListener extends OnHoursTimePickListener {

        void onHoursTimePicked(String hour, String minute);
    }

}

