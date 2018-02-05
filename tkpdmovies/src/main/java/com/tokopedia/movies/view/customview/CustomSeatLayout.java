package com.tokopedia.movies.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.view.presenter.SeatSelectionPresenter;


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

    static int numoFSeats;

    public CustomSeatLayout(Context context, SeatSelectionPresenter presenter) {
        super(context);
        this.mPresenter = presenter;
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
    }

    public void setText(String text, int status) {
        if (text.length() > 0 && status == 1) {
            individualSeat.setText(text);
            individualSeat.setClickable(true);
        } else if (text.length() > 0 && status == 2){
            individualSeat.setText(text);
            individualSeat.setClickable(false);
            individualSeat.setBackgroundResource(R.drawable.booked_seat_bg);
        } else if (status == 0 || text.length() == 0){
            individualSeat.setBackground(null);
            individualSeat.setClickable(false);
        }
    }

    @OnClick(R2.id.tv_seat)
    void seatClicked(){
        if (!individualSeat.isSelected()) {
            individualSeat.setSelected(true);
            individualSeat.setBackgroundResource(R.drawable.selected_seat_bg);
            numoFSeats++;
        } else if (individualSeat.isSelected()) {
            individualSeat.setSelected(false);
            individualSeat.setBackgroundResource(R.drawable.seat_bg);
            numoFSeats--;
        }
        mPresenter.setTicketPrice(numoFSeats);
    }

}
