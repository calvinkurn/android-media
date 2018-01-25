package com.tokopedia.movies.view.customview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.movies.R;
import com.tokopedia.movies.R2;
import com.tokopedia.movies.view.activity.EventBookTicketActivity;
import com.tokopedia.movies.view.activity.SeatSelectionActivity;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.PackageViewModel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

/**
 * Created by naveengoyal on 1/10/18.
 */

public class CustomShowTimeLayout extends LinearLayout {

    @BindView(R2.id.show_timing)
    TextView showTiming;


    SeatLayoutInfo seatLayoutInfo;

    public CustomShowTimeLayout(Context context) {
        super(context);
        initView();
    }

    public CustomShowTimeLayout(Context context, SeatLayoutInfo seatLayoutInfo) {
        super(context);
        this.seatLayoutInfo = seatLayoutInfo;
        initView();
    }

    public CustomShowTimeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomShowTimeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        inflate(getContext(), R.layout.include_show_timing, this);
        ButterKnife.bind(this);
    }

    public void setText(String text) {
        showTiming.setText(text);
        showTiming.setVisibility(VISIBLE);
        if (Utils.currentTime().compareTo(text) > 0) {
            showTiming.setBackgroundResource(R.drawable.show_timing_not_selectable);
//            showTiming.setClickable(false);
        }
    }

    @OnClick(R2.id.show_timing)
    public void clickedView() {
        Intent seatLayoutIntent = new Intent(getContext(), SeatSelectionActivity.class);
        seatLayoutIntent.putExtra("SEATINFO", seatLayoutInfo);
        getContext().startActivity(seatLayoutIntent);
    }
}
