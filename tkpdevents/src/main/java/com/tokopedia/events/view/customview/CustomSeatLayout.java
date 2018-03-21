package com.tokopedia.events.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by naveengoyal on 1/19/18.
 */

public class CustomSeatLayout extends LinearLayout {

    @BindView(R2.id.tv_seat)
    TextView individualSeat;


    SeatSelectionPresenter mPresenter;

    public static int numoFSeats;
    int maxCount;
    String rowName;
    String columnName;
    int rowId;
    public static List<String> selectedSeatList = new ArrayList<>();
    public static List<String> rowids = new ArrayList<>();

    public CustomSeatLayout(Context context) {
        super(context);
        initView();
    }

    public CustomSeatLayout(Context context, SeatSelectionPresenter presenter, int maxTicket, int rowId, String rowName) {
        super(context);
        this.mPresenter = presenter;
        maxCount = maxTicket;
        this.rowId = rowId;
        this.rowName = rowName;
        initView();
    }

    public CustomSeatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSeatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.individual_seat, this);
        ButterKnife.bind(this);
        mPresenter.setSelectedSeatText(selectedSeatList, rowids);
    }

    public void setText(String text, int status) {
        columnName = text;
        if (numoFSeats >= maxCount) {
            individualSeat.setClickable(false);
        }
        if (text.length() > 0 && status == 1) {
            individualSeat.setText(text);
            individualSeat.setClickable(true);
        } else if (text.length() > 0 && status == 2) {
            individualSeat.setText(text);
            individualSeat.setClickable(false);
            individualSeat.setBackgroundResource(R.drawable.booked_seat_bg);
        } else if (status == 0 || text.length() == 0) {
            individualSeat.setBackground(null);
            individualSeat.setClickable(false);
        }
    }

    @OnClick(R2.id.tv_seat)
    void seatClicked() {
        if (!individualSeat.isSelected() && numoFSeats < maxCount) {
            individualSeat.setSelected(true);
            individualSeat.setBackgroundResource(R.drawable.selected_seat_bg);
            individualSeat.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            numoFSeats++;
            if (rowName != null && rowName.length() > 0) {
                selectedSeatList.add("" + rowName + columnName);
            } else {
                selectedSeatList.add(columnName);
            }
            rowids.add(Integer.toString(rowId));
        } else if (individualSeat.isSelected()) {
            individualSeat.setSelected(false);
            individualSeat.setBackgroundResource(R.drawable.seat_bg);
            individualSeat.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            numoFSeats--;
            if (rowName != null && rowName.length() > 0) {
                selectedSeatList.remove("" + rowName + columnName);
            } else {
                selectedSeatList.remove(columnName);
            }
            rowids.remove(Integer.toString(rowId));
        } else {
            Toast.makeText(getContext(),
                    String.format(getContext().getString(R.string.more_seat_than_tiket_warning_toast), maxCount),
                    Toast.LENGTH_SHORT).show();
        }
        mPresenter.setTicketPrice(numoFSeats);
        mPresenter.setSeatData();
    }

    public static void destroy() {
        numoFSeats = 0;
        CustomSeatLayout.selectedSeatList.clear();
        CustomSeatLayout.rowids.clear();
    }
}
