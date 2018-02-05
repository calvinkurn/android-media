package com.tokopedia.movies.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.view.presenter.SeatSelectionPresenter;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by naveengoyal on 1/19/18.
 */

public class CustomSeatAreaLayout extends LinearLayout {


    @BindView(R2.id.seat_areaID)
    TextView seatAreaText;
    @BindView(R2.id.seatTextLayout)
    LinearLayout seatTextLayout;

    SeatSelectionPresenter mPresenter;

    public CustomSeatAreaLayout(Context context, SeatSelectionPresenter presenter) {
        super(context);
        this.mPresenter = presenter;
        initView();
    }

    public CustomSeatAreaLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSeatAreaLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.individual_seat_area_text, this);
        ButterKnife.bind(this);
    }

    public void setSeatRow(String text) {
        if (text.length() > 0) {
            seatAreaText.setText(text);
        } else {
            seatAreaText.setBackground(null);
            seatAreaText.setClickable(false);
        }
    }

    public void addColumn(String text, int status) {
        final CustomSeatLayout customSeatLayout = new CustomSeatLayout(getContext(), mPresenter);
        customSeatLayout.setText(text, status);
        seatTextLayout.addView(customSeatLayout);
    }
}
