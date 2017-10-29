package com.p_v.flexiblecalendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.entity.SelectedDateItem;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IDateCellViewDrawer;
import com.p_v.fliexiblecalendar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author p-v
 */
class FlexibleCalendarGridAdapter extends RecyclerView.Adapter<FlexibleCalendarGridAdapter.CellHolder> {

    private static final int SIX_WEEK_DAY_COUNT = 42;
    private int year;
    private int month;
    private Context context;
    private MonthDisplayHelper monthDisplayHelper;
    private Calendar calendar;
    private OnDateCellItemClickListener onDateCellItemClickListener;
    private SelectedDateItem selectedItem;
    private SelectedDateItem userSelectedDateItem;
    private IDateCellViewDrawer cellViewDrawer;
    private boolean showDatesOutsideMonth;
    private boolean decorateDatesOutsideMonth;
    private boolean disableAutoDateSelection;

    List<? extends Event> events;
    ArrayList<CellData> cellDatas = new ArrayList<>();
    int itemCount;

    public FlexibleCalendarGridAdapter(Context context, int year, int month,
                                       boolean showDatesOutsideMonth, boolean decorateDatesOutsideMonth, int startDayOfTheWeek,
                                       boolean disableAutoDateSelection) {
        this.context = context;
        setHasStableIds(true);
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.disableAutoDateSelection = disableAutoDateSelection;
        initialize(year, month, startDayOfTheWeek);
    }

    public void initialize(int year, int month, int startDayOfTheWeek) {
        this.year = year;
        this.month = month;
        this.monthDisplayHelper = new MonthDisplayHelper(year, month, startDayOfTheWeek);
        this.calendar = FlexibleCalendarHelper.getLocalizedCalendar(context);

        //calculate item count
        int weekStartDay = monthDisplayHelper.getWeekStartDay();
        int firstDayOfWeek = monthDisplayHelper.getFirstDayOfMonth();
        int diff = firstDayOfWeek - weekStartDay;
        int toAdd = diff < 0 ? (diff + 7) : diff;
        showDatesOutsideMonth = true;
        itemCount = showDatesOutsideMonth ? SIX_WEEK_DAY_COUNT
                : monthDisplayHelper.getNumberOfDaysInMonth()
                + toAdd;

        //gen day data
        for (int i = 0; i < itemCount; i++) {
            int row = i / 7;
            int col = i % 7;
            int day = monthDisplayHelper.getDayAt(row, col);
            cellDatas.add(new CellData(day, month, year, i));
        }
    }

    public void setEvents(List<? extends Event> events) {
        for (int i = 0; i < cellDatas.size(); i++) {
            CellData cellData = cellDatas.get(i);

            ArrayList<Event> list = new ArrayList<>();
            for (Event event : events) {
                if (cellData.day == event.getDay() && cellData.month == event.getMonth() && cellData.year == event.getYear()) {
                    list.add(event);
                }
            }

            cellData.setDayEvents(list);
            notifyItemChanged(i);
        }
    }

    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        BaseCellView cellView = cellViewDrawer.getCellView(0, null, null, viewType);
        if (cellView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            cellView = (BaseCellView) inflater.inflate(R.layout.square_cell_layout, null);
        }
        return new CellHolder(cellView);
    }

    @Override
    public int getItemViewType(int position) {
        int row = position / 7;
        int col = position % 7;

        boolean isWithinCurrentMonth = monthDisplayHelper.isWithinCurrentMonth(row, col);

        //compute cell type
        int cellType = BaseCellView.OUTSIDE_MONTH;
        //day at the current row and col
        int day = monthDisplayHelper.getDayAt(row, col);
        if (isWithinCurrentMonth) {
            //set to REGULAR if is within current month
            cellType = BaseCellView.REGULAR;
            if (disableAutoDateSelection) {
                if (userSelectedDateItem != null && userSelectedDateItem.getYear() == year
                        && userSelectedDateItem.getMonth() == month
                        && userSelectedDateItem.getDay() == day) {
                    //selected
                    cellType = BaseCellView.SELECTED;
                }
            } else {
                if (selectedItem != null && selectedItem.getYear() == year
                        && selectedItem.getMonth() == month
                        && selectedItem.getDay() == day) {
                    //selected
                    cellType = BaseCellView.SELECTED;
                }
            }
            if (calendar.get(Calendar.YEAR) == year
                    && calendar.get(Calendar.MONTH) == month
                    && calendar.get(Calendar.DAY_OF_MONTH) == day) {
                if (cellType == BaseCellView.SELECTED) {
                    //today and selected
                    cellType = BaseCellView.SELECTED_TODAY;
                } else {
                    //today
                    cellType = BaseCellView.TODAY;
                }
            }
        }

        return cellType;
    }

    @Override
    public void onBindViewHolder(CellHolder holder, int position) {
        CellData cellData = cellDatas.get(position);

        drawDateCell(holder.baseCellView, cellData);
    }

    private void drawDateCell(BaseCellView cellView, CellData cellData) {
        int day = cellData.day;
        int position = cellData.position;
        int cellType = getItemViewType(position);

        if (cellType != BaseCellView.OUTSIDE_MONTH) {
            cellView.setText(String.valueOf(day));
            cellView.setOnClickListener(new DateClickListener(day, month, year, position));
            cellView.setEvents(cellData.dayEvents);
            switch (cellType) {
                case BaseCellView.SELECTED_TODAY:
                    cellView.addState(BaseCellView.STATE_TODAY);
                    cellView.addState(BaseCellView.STATE_SELECTED);
                    break;
                case BaseCellView.TODAY:
                    cellView.addState(BaseCellView.STATE_TODAY);
                    break;
                case BaseCellView.SELECTED:
                    cellView.addState(BaseCellView.STATE_SELECTED);
                    break;
                default:
                    cellView.addState(BaseCellView.STATE_REGULAR);
            }
        } else {
            if (showDatesOutsideMonth) {
                cellView.setText(String.valueOf(day));
                int[] temp = new int[2];
                //date outside month and less than equal to 12 means it belongs to next month otherwise previous
                if (day <= 12) {
                    FlexibleCalendarHelper.nextMonth(year, month, temp);
                    cellView.setOnClickListener(new DateClickListener(day, temp[1], temp[0], position));
                } else {
                    FlexibleCalendarHelper.previousMonth(year, month, temp);
                    cellView.setOnClickListener(new DateClickListener(day, temp[1], temp[0], position));
                }

                if (decorateDatesOutsideMonth && cellData.dayEvents != null) {
                    cellView.setEvents(cellData.dayEvents);
                }

                cellView.addState(BaseCellView.STATE_OUTSIDE_MONTH);
            } else {
                cellView.setBackgroundResource(android.R.color.transparent);
                cellView.setText(null);
                cellView.setOnClickListener(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setOnDateClickListener(OnDateCellItemClickListener onDateCellItemClickListener) {
        this.onDateCellItemClickListener = onDateCellItemClickListener;
    }

    public void setSelectedItem(SelectedDateItem selectedItem, boolean notify, boolean isUserSelected) {
        this.selectedItem = selectedItem;
        if (disableAutoDateSelection && isUserSelected) {
            this.userSelectedDateItem = selectedItem;
        }
        if (notify) notifyDataSetChanged();
    }

    public SelectedDateItem getSelectedItem() {
        return selectedItem;
    }

    void setMonthEventFetcher(MonthEventFetcher monthEventFetcher) {
//        this.monthEventFetcher = monthEventFetcher;
    }

    public void setCellViewDrawer(IDateCellViewDrawer cellViewDrawer) {
        this.cellViewDrawer = cellViewDrawer;
    }

    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.notifyDataSetChanged();
    }

    public void setDecorateDatesOutsideMonth(boolean decorateDatesOutsideMonth) {
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.notifyDataSetChanged();
    }

    public void setDisableAutoDateSelection(boolean disableAutoDateSelection) {
        this.disableAutoDateSelection = disableAutoDateSelection;
        this.notifyDataSetChanged();
    }

    public void setFirstDayOfTheWeek(int firstDayOfTheWeek) {
        monthDisplayHelper = new MonthDisplayHelper(year, month, firstDayOfTheWeek);
        this.notifyDataSetChanged();
    }

    public SelectedDateItem getUserSelectedItem() {
        return userSelectedDateItem;
    }

    public void setUserSelectedDateItem(SelectedDateItem selectedItem) {
        this.userSelectedDateItem = selectedItem;
        notifyDataSetChanged();
    }

    public List<? extends Event> getEvents(int day) {
        for (CellData cellData : cellDatas) {
            if (cellData.day == day) {
                return cellData.dayEvents;
            }
        }
        return null;
    }

    public interface OnDateCellItemClickListener {
        void onDateClick(SelectedDateItem selectedItem);
    }

    interface MonthEventFetcher {
        List<? extends Event> getEventsForTheDay(int year, int month, int day);
    }

    public void refresh(SelectedDateItem newSelectedItem) {
        int newPos;
        newPos = newSelectedItem.getPosition();
        notifyItemChanged(newPos);
    }

    private class CellData {
        int day;
        int month;
        int year;
        List<? extends Event> dayEvents;
        int position;

        public CellData(int day, int month, int year, int position) {
            this.day = day;
            this.month = month;
            this.year = year;
            this.position = position;
        }

        public void setDayEvents(List<? extends Event> dayEvents) {
            this.dayEvents = dayEvents;
        }
    }

    private class DateClickListener implements View.OnClickListener {

        private int iDay;
        private int iMonth;
        private int iYear;
        private int iPosition;

        public DateClickListener(int day, int month, int year, int position) {
            this.iDay = day;
            this.iMonth = month;
            this.iYear = year;
            this.iPosition = position;
        }

        @Override
        public void onClick(final View v) {
            selectedItem = new SelectedDateItem(iYear, iMonth, iDay, iPosition);

            refresh(selectedItem);

            if (disableAutoDateSelection) {
                userSelectedDateItem = selectedItem;
            }

            if (onDateCellItemClickListener != null) {
                onDateCellItemClickListener.onDateClick(selectedItem);
            }
        }
    }

    public static class CellHolder extends RecyclerView.ViewHolder {

        BaseCellView baseCellView;

        public CellHolder(BaseCellView itemView) {
            super(itemView);
            this.baseCellView = itemView;
        }
    }

}
