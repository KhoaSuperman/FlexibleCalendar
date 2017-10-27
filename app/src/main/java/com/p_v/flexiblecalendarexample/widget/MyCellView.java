package com.p_v.flexiblecalendarexample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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

    ArrayList<MyEvent> myEvents = new ArrayList<>();
    Drawable dfBg;
    int dfTextColor = 0;
    static Drawable dfBgSelected;
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
        dfBg = getBackground();
        dfTextColor = getCurrentTextColor();

        if (dfBgSelected == null) {
            dfBgSelected = getContext().getResources().getDrawable(R.drawable.inset_cell_selected);
            dfTextWhite = getContext().getResources().getColor(android.R.color.white);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getStateSet().contains(STATE_SELECTED)) {
            setBackground(dfBgSelected);
            setTextColor(dfTextWhite);
        } else {
            if (myEvents.size() > 0) {
                for (MyEvent myEvent : myEvents) {
                    setBackground(getContext().getResources().getDrawable(myEvent.getBgColor()));
                    setTextColor(getContext().getResources().getColor(android.R.color.white));
                }
            } else {
                setBackground(dfBg);
                setTextColor(dfTextColor);
            }
        }
    }

    public void setEvents(List<? extends Event> events) {
        myEvents.clear();
        if (events != null) {
            for (Event event : events) {
                MyEvent myEvent = (MyEvent) event;
                if (myEvent.getColor() == 0)
                    this.myEvents.add(myEvent);
            }
        }

        super.setEvents(events);
    }
}
