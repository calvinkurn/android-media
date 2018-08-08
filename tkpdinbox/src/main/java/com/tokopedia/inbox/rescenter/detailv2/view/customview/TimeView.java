package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.util.MethodChecker;
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
    View timeTickerView;
    TextView tvTitle;
    Button btnGetHelp;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        tvTitle = view.findViewById(R.id.tv_title);
        timeTickerView = findViewById(R.id.time_ticker);
        btnGetHelp = view.findViewById(R.id.btn_get_help);
    }

    @Override
    public void setListener(DetailResCenterFragmentView detailResCenterFragmentView) {
        this.listener = detailResCenterFragmentView;
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
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull DetailData detailData) {
        btnGetHelp.setVisibility(GONE);
        timeTickerView.setVisibility(GONE);
        long duration = getDuration(detailData.getResponseDeadline());
        if (duration > 0 && !MethodChecker.isTimezoneNotAutomatic()) {
            setVisibility(VISIBLE);
            timeTickerView.setVisibility(VISIBLE);
            if (timeTickerUtil == null) {
                timeTickerUtil = TimeTickerUtil.createInstance(timeTickerView,
                        getTimeTickerListener());
            }
            tvTitle.setText(MethodChecker.fromHtml(getResources().getString(R.string.string_help_info)));
            timeTickerUtil.startTimer(duration);
        } else if (MethodChecker.isTimezoneNotAutomatic()) {
            setVisibility(GONE);
            ServerErrorHandler.showTimezoneErrorSnackbar();
        } else if (detailData.isCanAskHelp()) {
            setVisibility(VISIBLE);
            btnGetHelp.setVisibility(VISIBLE);
            tvTitle.setText(MethodChecker.fromHtml(getResources().getString(R.string
                    .string_help_info_2)));
            btnGetHelp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.setOnActionHelpTimeViewClick();
                }
            });
        } else {
            setVisibility(GONE);
        }
    }

    private void refreshMainPage() {
        timeTickerView.setVisibility(GONE);
        btnGetHelp.setVisibility(VISIBLE);
        listener.hideTimeTicker();
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
