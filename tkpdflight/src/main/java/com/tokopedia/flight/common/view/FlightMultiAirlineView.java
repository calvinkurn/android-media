package com.tokopedia.flight.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.flight.R;

import java.util.ArrayList;
import java.util.List;

public class FlightMultiAirlineView extends LinearLayout {
    List<String> airlineLogoList;

    public FlightMultiAirlineView(Context context) {
        super(context);
    }

    public FlightMultiAirlineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlightMultiAirlineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlightMultiAirlineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setAirlineLogo(String airlineLogo) {
        this.airlineLogoList = new ArrayList<>();
        this.airlineLogoList.add(airlineLogo);
        updateViewByAirlineLogo();
    }

    public void setAirlineLogos(List<String> airlineLogos) {
        this.airlineLogoList = airlineLogos;
        updateViewByAirlineLogo();
    }

    private void updateViewByAirlineLogo() {
        this.removeAllViews();
        if (airlineLogoList == null || airlineLogoList.size() == 0) {
            airlineLogoList = new ArrayList<>();
            airlineLogoList.add("");
        } else if (airlineLogoList.size() > 1) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_airline_logo, this, false);
            ImageView ivAirline = view.findViewById(R.id.iv_airline_logo);
            ivAirline.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_multi_airlines));
            addView(ivAirline);
        } else if (airlineLogoList.size() == 1) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_airline_logo, this, false);
            ImageView ivAirline = view.findViewById(R.id.iv_airline_logo);
            ImageHandler.loadImageWithoutPlaceholder(ivAirline, airlineLogoList.get(0),
                    R.drawable.ic_airline_default);
            addView(view);
        }
    }

}
