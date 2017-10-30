package com.p_v.flexiblecalendarexample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendarexample.MyEvent;
import com.p_v.flexiblecalendar.view.CircularEventCellView;
import com.p_v.flexiblecalendarexample.R;

import java.util.List;

/**
 * @author p-v
 */
public class MyCellView extends CircularEventCellView {

    MyEvent myEvent;
    int dfTextColor = 0;
    static int dfBgSelected;
    static int dfTextWhite = 0;
    static int selectedPadding;
    static int customHeight;

    public MyCellView(Context context) {
        super(context);
        init();
    }

    public MyCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dfTextColor = getCurrentTextColor();

        if (dfBgSelected == 0) {
            dfBgSelected = ContextCompat.getColor(getContext(), R.color.black);
            dfTextWhite = ContextCompat.getColor(getContext(), android.R.color.white);
            selectedPadding = (int) (getContext().getResources().getDisplayMetrics().density * 6);
            customHeight = (int) (getContext().getResources().getDisplayMetrics().density * 40);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(widthSize, customHeight);
        eventCircleY = customHeight - radius;
    }

    Paint pSelected;
    RectF rectSelected;

    Paint pEvent;

    @Override
    protected void onDraw(Canvas canvas) {
        if (pSelected == null) {
            pSelected = new Paint();
            pSelected.setAntiAlias(true);
            pSelected.setColor(dfBgSelected);

            float textSize = getPaint().measureText("31", 0, 2) + selectedPadding;
            int left = (int) ((getWidth() - textSize) / 2);
            int top = (int) ((getHeight() - textSize) / 2);
            int right = (int) (left + textSize);
            int bottom = (int) (top + textSize);
            rectSelected = new RectF(left, top, right, bottom);
        }

        if (getStateSet().contains(STATE_SELECTED)) {
            setTextColor(dfTextWhite);
            canvas.drawRoundRect(rectSelected, 6, 6, pSelected);
        } else {
            if (myEvent != null) {
                if (pEvent == null) {
                    pEvent = new Paint();
                    pEvent.setAntiAlias(true);
                    pEvent.setColor(myEvent.getBgColor());
                }
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 4 + selectedPadding / 2, pEvent);
                setTextColor(dfTextWhite);
            } else {
                setTextColor(dfTextColor);
            }
        }

        super.onDraw(canvas);
    }

    public void setEvents(List<? extends Event> events) {
        myEvent = null;
        if (events != null) {
            for (Event event : events) {
                MyEvent myEvent = (MyEvent) event;
                if (myEvent.getColor() == 0)
                    this.myEvent = myEvent;
            }
        }

        super.setEvents(events);
    }
}
