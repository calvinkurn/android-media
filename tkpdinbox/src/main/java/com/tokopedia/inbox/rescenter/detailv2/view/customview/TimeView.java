package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.create.customview.BaseView;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;
import com.tokopedia.inbox.rescenter.utils.TimeTickerUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yfsx on 07/11/17.
 */

public class TimeView extends BaseView<DetailData, DetailResCenterFragmentView> {

    TimeTickerUtil timeTickerUtil;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        View timeTickerView = findViewById(R.id.time_ticker);
        if (timeTickerUtil == null) {
            timeTickerUtil = TimeTickerUtil.createInstance(timeTickerView,
                    getTimeTickerListener());
        }
    }

    @Override
    public void setListener(DetailResCenterFragmentView detailResCenterFragmentView) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_time_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull DetailData detailData) {
        long duration = getDuration(detailData.getResponseDeadline());
        if (duration > 0) {
            timeTickerUtil.startTimer(duration);
        } else {
            refreshMainPage();
        }
    }

    private void refreshMainPage() {

    }

    private TimeTickerUtil.TimeTickerListener getTimeTickerListener() {
        return new TimeTickerUtil.TimeTickerListener() {
            @Override
            public void onStart() {
                Log.d(TimeView.class.getSimpleName(), "onStart Ticker");
            }

            @Override
            public void onFinish() {
                Log.d(TimeView.class.getSimpleName(), "onFinish Ticker");
                refreshMainPage();
            }
        };
    }

    private long getDuration(String deliveryDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(deliveryDate);
            Date currentTime = new Date();
            long duration = date.getTime() - currentTime.getTime();
            return duration;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
