package com.mistong.android.timepickerlibrary.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mistong.android.timepickerlibrary.R;
import com.mistong.android.timepickerlibrary.utils.ConvertUtils;


/**
 * 带确定及取消按钮的弹窗
 */
@SuppressWarnings("WeakerAccess")
public abstract class ConfirmPopup<V extends View> extends BasicPopup<View> {
    protected boolean topLineVisible = true;
    protected int topLineColor = 0xFF33B5E5;
    protected int topLineHeightPixels = 1;//px
    protected int topBackgroundColor = Color.WHITE;
    protected int topHeight = 40;//dp
    protected int topPadding = 15;//dp
    protected int contentLeftAndRightPadding = 0;//dp
    protected int contentTopAndBottomPadding = 0;//dp
    protected boolean cancelVisible = true;
    protected CharSequence cancelText = "";
    protected CharSequence submitText = "";
    protected CharSequence titleText = "";
    protected int leftTextColor = 0xFF33B5E5;
    protected int rightTextColor = 0xFF33B5E5;
    protected int titleTextColor = Color.BLACK;
    protected int pressedTextColor = 0XFF0288CE;
    protected int leftTextSize = 0;
    protected int rightTextSize = 0;
    protected int titleTextSize = 0;
    protected int rightWidth = 58;
    protected int rightHeight = 27;
    protected int backgroundColor = Color.WHITE;
    protected Drawable rightBackgroundDrawable;
    protected TextView leftButton, rightButton, closeButton;
    protected View titleView, divideLineView;
    protected View headerView, centerView, footerView;
    protected boolean theSelectTimeIsEffective = false;

    public ConfirmPopup(Activity activity) {
        super(activity);
        cancelText = activity.getString(android.R.string.cancel);
        submitText = activity.getString(android.R.string.ok);
    }

    /**
     * 设置顶部标题栏下划线颜色
     */
    public void setTopLineColor(@ColorInt int topLineColor) {
        this.topLineColor = topLineColor;
    }

    /**
     * 设置顶部标题栏下划线高度，单位为px
     */
    public void setTopLineHeight(int topLineHeightPixels) {
        this.topLineHeightPixels = topLineHeightPixels;
    }

    /**
     * 设置顶部标题栏背景颜色
     */
    public void setTopBackgroundColor(@ColorInt int topBackgroundColor) {
        this.topBackgroundColor = topBackgroundColor;
    }

    /**
     * 设置顶部标题栏高度（单位为dp）
     */
    public void setTopHeight(@IntRange(from = 10, to = 80) int topHeight) {
        this.topHeight = topHeight;
    }

    /**
     * 设置顶部按钮左边及右边边距（单位为dp）
     */
    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    /**
     * 设置顶部标题栏下划线是否显示
     */
    public void setTopLineVisible(boolean topLineVisible) {
        this.topLineVisible = topLineVisible;
    }

    /**
     * 设置内容上下左右边距（单位为dp）
     */
    public void setContentPadding(int leftAndRight, int topAndBottom) {
        this.contentLeftAndRightPadding = leftAndRight;
        this.contentTopAndBottomPadding = topAndBottom;
    }

    /**
     * 设置顶部标题栏取消按钮是否显示
     */
    public void setCancelVisible(boolean cancelVisible) {
        if (null != leftButton) {
            leftButton.setVisibility(cancelVisible ? View.VISIBLE : View.GONE);
        } else {
            this.cancelVisible = cancelVisible;
        }
    }

    /**
     * 设置顶部标题栏取消按钮文字
     */
    public void setCancelText(CharSequence cancelText) {
        if (null != leftButton) {
            leftButton.setText(cancelText);
        } else {
            this.cancelText = cancelText;
        }
    }

    /**
     * 设置顶部标题栏取消按钮文字
     */
    public void setCancelText(@StringRes int textRes) {
        setCancelText(activity.getString(textRes));
    }

    /**
     * 设置顶部标题栏确定按钮文字
     */
    public void setSubmitText(CharSequence submitText) {
        if (null != rightButton) {
            rightButton.setText(submitText);
        } else {
            this.submitText = submitText;
        }
    }

    /**
     * 设置顶部标题栏确定按钮文字
     */
    public void setSubmitText(@StringRes int textRes) {
        setSubmitText(activity.getString(textRes));
    }

    /**
     * 设置顶部标题栏右侧按钮宽度（单位为dp）
     */
    public void setRightWidth(@IntRange(from = 10, to = 80) int rightWidth) {
        this.rightWidth = rightWidth;
    }

    /**
     * 设置顶部标题栏右侧按钮高度（单位为dp）
     */
    public void setRightHeight(@IntRange(from = 10, to = 80) int rightHeight) {
        this.rightHeight = rightHeight;
    }

    /**
     * 设置顶部标题栏右侧按钮背景颜色
     */
    public void setRightBackgroundDrawable(Drawable rightBackgroundDrawable) {
        this.rightBackgroundDrawable = rightBackgroundDrawable;
    }

    /**
     * 设置顶部标题栏右侧按钮背景颜色
     */
    public void setRightBackgroundResource(int rightBackgroundDrawable) {
        this.rightButton.setBackgroundResource(rightBackgroundDrawable);
    }

    /**
     * 设置顶部标题栏标题文字
     */
    public void setTitleText(CharSequence titleText) {
        if (titleView != null && titleView instanceof TextView) {
            ((TextView) titleView).setText(titleText);
        } else {
            this.titleText = titleText;
        }
    }

    /**
     * 设置顶部标题栏标题文字
     */
    public void setTitleText(@StringRes int textRes) {
        setTitleText(activity.getString(textRes));
    }

    /**
     * 设置顶部标题栏取消按钮文字颜色
     */
    public void setLeftTextColor(@ColorInt int leftTextColor) {
        if (null != leftButton) {
            leftButton.setTextColor(leftTextColor);
        } else {
            this.leftTextColor = leftTextColor;
        }
    }

    /**
     * 设置顶部标题栏取消按钮点击文字颜色
     */
    public void setCancelPressTextColor(@ColorInt int pressTextColor) {
        if (null != leftButton) {
            leftButton.setTextColor(pressedTextColor);
        } else {
            this.pressedTextColor = pressTextColor;
        }
    }

    /**
     * 设置顶部标题栏确定按钮文字颜色
     */
    public void setRightTextColor(@ColorInt int rightTextColor) {
        if (null != rightButton) {
            rightButton.setTextColor(rightTextColor);
        } else {
            this.rightTextColor = rightTextColor;
        }
    }

    /**
     * 设置顶部标题栏标题文字颜色
     */
    public void setTitleTextColor(@ColorInt int titleTextColor) {
        if (null != titleView && titleView instanceof TextView) {
            ((TextView) titleView).setTextColor(titleTextColor);
        } else {
            this.titleTextColor = titleTextColor;
        }
    }

    /**
     * 设置按下时的文字颜色
     */
    public void setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
    }

    /**
     * 设置顶部标题栏取消按钮文字大小（单位为sp）
     */
    public void setLeftTextSize(@IntRange(from = 10, to = 40) int leftTextSize) {
        this.leftTextSize = leftTextSize;
    }

    /**
     * 设置顶部标题栏确定按钮文字大小（单位为sp）
     */
    public void setRightTextSize(@IntRange(from = 10, to = 40) int rightTextSize) {
        this.rightTextSize = rightTextSize;
    }

    /**
     * 设置顶部标题栏标题文字大小（单位为sp）
     */
    public void setTitleTextSize(@IntRange(from = 10, to = 40) int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    /**
     * 设置选择器主体背景颜色
     */
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTitleView(View titleView) {
        this.titleView = titleView;
    }

    public View getTitleView() {
        if (null == titleView) {
            throw new NullPointerException("please call show at first");
        }
        return titleView;
    }

    public TextView getLeftButton() {
        if (null == leftButton) {
            throw new NullPointerException("please call show at first");
        }
        return leftButton;
    }

    public TextView getRightButton() {
        if (null == rightButton) {
            throw new NullPointerException("please call show at first");
        }
        return rightButton;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }


    /**
     * 设置你自定义的时间是否是有效的
     */
    public void setTheSelectTimeIsEffective(boolean theSelectTimeIsEffective) {
        this.theSelectTimeIsEffective = theSelectTimeIsEffective;
    }

    /**
     * @see #makeHeaderView()
     * @see #makeCenterView()
     * @see #makeFooterView()
     */
    @Override
    protected final View makeContentView() {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setBackgroundColor(backgroundColor);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setPadding(0, 0, 0, 0);
        rootLayout.setClipToPadding(false);
        View headerView = makeHeaderView();
        if (headerView != null) {
            rootLayout.addView(headerView);
        }
        if (topLineVisible) {
            View lineView = new View(activity);
            lineView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, topLineHeightPixels));
            lineView.setBackgroundColor(topLineColor);
            rootLayout.addView(lineView);
        }
        if (centerView == null) {
            centerView = makeCenterView();
        }
        int lr = 0;
        int tb = 0;
        if (contentLeftAndRightPadding > 0) {
            lr = ConvertUtils.toPx(activity, contentLeftAndRightPadding);
        }
        if (contentTopAndBottomPadding > 0) {
            tb = ConvertUtils.toPx(activity, contentTopAndBottomPadding);
        }
        centerView.setPadding(lr, tb, lr, tb);
        ViewGroup vg = (ViewGroup) centerView.getParent();
        if (vg != null) {
            //IllegalStateException: The specified child already has a parent
            vg.removeView(centerView);
        }
        rootLayout.addView(centerView, new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1.0f));
        View footerView = makeFooterView();
        if (footerView != null) {
            rootLayout.addView(footerView);
        }
        return rootLayout;
    }

    @Nullable
    protected View makeHeaderView() {
        if (null != headerView) {
            return headerView;
        }
        RelativeLayout topButtonLayout = new RelativeLayout(activity);
        int height = ConvertUtils.toPx(activity, topHeight);
        topButtonLayout.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, height));
        topButtonLayout.setBackgroundColor(topBackgroundColor);
        topButtonLayout.setGravity(Gravity.CENTER_VERTICAL);

        leftButton = new TextView(activity);
        leftButton.setVisibility(cancelVisible ? View.VISIBLE : View.GONE);
        RelativeLayout.LayoutParams cancelParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        cancelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        cancelParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftButton.setLayoutParams(cancelParams);
        leftButton.setBackgroundColor(Color.TRANSPARENT);
        leftButton.setGravity(Gravity.CENTER);
        int padding = ConvertUtils.toPx(activity, topPadding);
        leftButton.setPadding(padding, 0, padding, 0);
        if (!TextUtils.isEmpty(cancelText)) {
            leftButton.setText(cancelText);
        }
        leftButton.setTextColor(ConvertUtils.toColorStateList(leftTextColor, pressedTextColor));
        if (leftTextSize != 0) {
            leftButton.setTextSize(leftTextSize);
        }
        topButtonLayout.addView(leftButton);

        if (null == titleView) {
            TextView textView = new TextView(activity);
            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            int margin = ConvertUtils.toPx(activity, topPadding);
            titleParams.leftMargin = margin;
            titleParams.rightMargin = margin;
            titleParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            titleParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            textView.setLayoutParams(titleParams);
            textView.setGravity(Gravity.CENTER);
            if (!TextUtils.isEmpty(titleText)) {
                textView.setText(titleText);
            }
            textView.setTextColor(titleTextColor);
            if (titleTextSize != 0) {
                textView.setTextSize(titleTextSize);
            }
            titleView = textView;
        }
        topButtonLayout.addView(titleView);

        rightButton = new TextView(activity);
        int mSubmitWidth = ConvertUtils.toPx(activity, rightWidth);
        int mSubmitheight = ConvertUtils.toPx(activity, rightHeight);
        RelativeLayout.LayoutParams submitParams = new RelativeLayout.LayoutParams(mSubmitWidth, mSubmitheight);
        submitParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        submitParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        submitParams.rightMargin = ConvertUtils.toPx(getContext(), 12);

        rightButton.setLayoutParams(submitParams);
        rightButton.setBackgroundDrawable(rightBackgroundDrawable);
        rightButton.setGravity(Gravity.CENTER);
        rightButton.setPadding(padding, 0, padding, 0);
        if (!TextUtils.isEmpty(submitText)) {
            rightButton.setText(submitText);
        }
        rightButton.setTextColor(ConvertUtils.toColorStateList(rightTextColor, rightTextColor));
        if (rightTextSize != 0) {
            rightButton.setTextSize(rightTextSize);
        }
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theSelectTimeIsEffective) {
                    dismiss();
                    onSubmit();
                }
            }
        });
        topButtonLayout.addView(rightButton);
        return topButtonLayout;
    }

    @NonNull
    protected abstract V makeCenterView();

    @Nullable
    protected View makeFooterView() {
        if (null != footerView) {
            return footerView;
        }
        LinearLayout footCloseLayout = new LinearLayout(activity);
        footCloseLayout.setOrientation(LinearLayout.VERTICAL);
        int height = ConvertUtils.toPx(activity, 63);
        footCloseLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, height));
        footCloseLayout.setBackgroundColor(topBackgroundColor);
        divideLineView = new View(activity);
        LinearLayout.LayoutParams lineparams = new LinearLayout.LayoutParams(MATCH_PARENT, ConvertUtils.toPx(activity, 1));
        lineparams.leftMargin = ConvertUtils.toPx(getContext(), 12);
        lineparams.rightMargin = ConvertUtils.toPx(getContext(), 12);
        divideLineView.setLayoutParams(lineparams);
        divideLineView.setBackgroundColor(getContext().getResources().getColor(R.color.color_f8f9fc));
        footCloseLayout.addView(divideLineView);

        closeButton = new TextView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        closeButton.setLayoutParams(params);
        closeButton.setGravity(Gravity.CENTER);
        closeButton.setText("关闭");
        closeButton.setTextSize(16);
        closeButton.setTextColor(getContext().getResources().getColor(R.color.color_434f59));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        footCloseLayout.addView(closeButton);
        return footCloseLayout;
    }

    protected void onSubmit() {

    }

    protected void onCancel() {

    }

}
