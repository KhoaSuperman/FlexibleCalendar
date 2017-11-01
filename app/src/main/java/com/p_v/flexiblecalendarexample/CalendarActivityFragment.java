package com.p_v.flexiblecalendarexample;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.CellData;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.SquareCellView;
import com.p_v.flexiblecalendarexample.widget.MyCellView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class CalendarActivityFragment extends Fragment implements FlexibleCalendarView.OnMonthChangeListener, FlexibleCalendarView.OnDateClickListener {

    private FlexibleCalendarView calendarView;
    private TextView someTextView;
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Bangkok"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        calendar.add(Calendar.DAY_OF_MONTH, 2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setShowDatesOutsideMonth(true);
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, @BaseCellView.CellType int cellType) {
                MyCellView cellView;
                int layout;
                switch (cellType) {
                    case BaseCellView.OUTSIDE_MONTH:
                        layout = R.layout.item_cell_day_outside;
                        break;
                    case BaseCellView.TODAY:
                        layout = R.layout.item_cell_today;
                        break;
                    default:
                        layout = R.layout.item_cell_normal;
                        break;
                }
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                cellView = (MyCellView) inflater.inflate(layout, null);
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    cellView = (SquareCellView) inflater.inflate(com.p_v.fliexiblecalendar.R.layout.square_cell_layout, null);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnDateClickListener(this);

        fillEvents();

        setupToolBar(view);

        return view;
    }

    private void fillEvents() {
        //NOTE: each time month change, need refill event data
        List<MyEvent> eventColors = new ArrayList<>(5);
        eventColors.add(new MyEvent(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light), 17, 9, 2017));
        eventColors.add(new MyEvent(ContextCompat.getColor(getContext(), android.R.color.holo_purple), 22, 9, 2017));
        eventColors.add(new MyEvent(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark), 29, 9, 2017));
        eventColors.add(new MyEvent(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark), 29, 9, 2017));

        MyEvent offShift = new MyEvent(0, 29, 9, 2017);
        offShift.setBgColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
        eventColors.add(offShift);

        calendarView.setEvents(9, 2017, eventColors);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateTitle(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth());
    }

    public void setupToolBar(View mainView) {
        Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowCustomEnabled(true);

        someTextView = new TextView(getActivity());
        someTextView.setTextColor(getActivity().getResources().getColor(R.color.title_text_color_activity_1));
        someTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarView.isShown()) {
                    calendarView.collapse();
                } else {
                    calendarView.expand();
                }
            }
        });
        bar.setCustomView(someTextView);

        bar.setBackgroundDrawable(new ColorDrawable(getActivity().getResources()
                .getColor(R.color.action_bar_color_activity_1)));

        //back button color
//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(R.color.title_text_color_activity_1), PorterDuff.Mode.SRC_ATOP);
//        bar.setHomeAsUpIndicator(upArrow);
    }

    private void updateTitle(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        someTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
                this.getResources().getConfiguration().locale) + " " + year);
    }

    @Override
    public void onMonthChange(int year, int month, int direction) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        updateTitle(year, month);
    }

    @Override
    public void onDateClick(CellData cellData) {
        Calendar cal = Calendar.getInstance();
        cal.set(cellData.year, cellData.month, cellData.day);

        List<? extends Event> events = calendarView.getEventsForTheDay(cellData.year, cellData.month, cellData.day);
        if (events != null) {
            for (Event event : events) {
                MyEvent calendarEvent = (MyEvent) event;
                Log.d("Event", calendarEvent.getColor() + "");
            }

            Log.d("Event", "----");
        }
    }
}
