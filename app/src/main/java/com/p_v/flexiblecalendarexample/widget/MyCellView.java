package com.p_v.flexiblecalendarexample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendarexample.MyEvent;
import com.p_v.flexiblecalendar.view.CircularEventCellView;
import com.p_v.flexiblecalendarexample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author p-v
 */
public class MyCellView extends CircularEventCellView {

    MyEvent myEvent;
    int dfBg;
    int dfTextColor = 0;
    static int dfBgSelected;
    static int dfTextWhite = 0;

    public MyCellView(Context context) {
        super(context);
        init();
    }

    public MyCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
//        dfBg = getBackground();
        dfTextColor = getCurrentTextColor();

        if (dfBgSelected == 0) {
            dfBgSelected = ContextCompat.getColor(getContext(), R.color.black);
            dfTextWhite = ContextCompat.getColor(getContext(), android.R.color.white);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    Paint pSelected;
    RectF rectSelected;
    int offset = 35;

    Paint pEvent;

    @Override
    protected void onDraw(Canvas canvas) {
        if (pSelected == null) {
            pSelected = new Paint();
            pSelected.setAntiAlias(true);
            pSelected.setColor(dfBgSelected);

            rectSelected = new RectF(offset, offset, getWidth() - offset, getHeight() - offset);
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
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 4, pEvent);
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
