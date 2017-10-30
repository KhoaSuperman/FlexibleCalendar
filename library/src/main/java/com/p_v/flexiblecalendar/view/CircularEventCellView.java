package com.p_v.flexiblecalendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.fliexiblecalendar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author p-v
 */
public class CircularEventCellView extends BaseCellView {

    protected int eventCircleY;
    protected int radius;
    private int padding;
    private int leftMostPosition = Integer.MIN_VALUE;
    private List<Paint> paintList = new ArrayList<>();

    public CircularEventCellView(Context context) {
        super(context);
    }

    public CircularEventCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularEventCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularEventCellView);
        try {
            int dfRadius = (int) (getContext().getResources().getDisplayMetrics().density * 3);
            radius = (int) a.getDimension(R.styleable.CircularEventCellView_event_radius, dfRadius);
            padding = (int) a.getDimension(R.styleable.CircularEventCellView_event_circle_padding, 1);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int num = paintList.size();

        eventCircleY = heightSize - radius;

        //calculate left most position for the circle
        leftMostPosition = (widthSize / 2) - (num / 2) * 2 * (padding + radius);
        if (num % 2 == 0) {
            leftMostPosition = leftMostPosition + radius + padding;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paintList != null) {
            int num = paintList.size();
            for (int i = 0; i < num; i++) {
                canvas.drawCircle(calculateStartPoint(i), eventCircleY, radius, paintList.get(i));
            }
        }
    }

    private int calculateStartPoint(int offset) {
        return leftMostPosition + offset * (2 * (radius + padding));
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        if (colorList != null) {
            paintList = new ArrayList<>(colorList.size());
            for (Event e : colorList) {
                if (e.getColor() != 0) {
                    Paint eventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    eventPaint.setStyle(Paint.Style.FILL);
                    eventPaint.setColor(e.getColor());
                    paintList.add(eventPaint);
                }
            }
            invalidate();
            requestLayout();
        } else {
            if (paintList != null)
                paintList.clear();

            invalidate();
            requestLayout();
        }
    }

}
