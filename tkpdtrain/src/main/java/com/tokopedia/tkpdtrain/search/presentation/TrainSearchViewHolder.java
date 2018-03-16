package com.tokopedia.tkpdtrain.search.presentation;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.util.TrainDateUtil;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/15/18.
 */

public class TrainSearchViewHolder extends AbstractViewHolder<TrainScheduleViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_train_schedule;

    private LinearLayout flagItemLayout;
    private TextView trainNameTv;
    private TextView classNameTv;
    private TextView originCodeTv;
    private TextView departureTimeTv;
    private TextView destinationCodeTv;
    private TextView arrivalTimeTv;
    private TextView durationTv;
    private TextView availabilitySeatTv;
    private TextView priceTv;
    private TextView detailScheduleTv;
    private TrainSearchAdapterTypeFactory.OnTrainSearchListener listener;
    private Context context;

    public TrainSearchViewHolder(View itemView, TrainSearchAdapterTypeFactory.OnTrainSearchListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        flagItemLayout = (LinearLayout) itemView.findViewById(R.id.flag_item);
        trainNameTv = (TextView) itemView.findViewById(R.id.train_name);
        classNameTv = (TextView) itemView.findViewById(R.id.class_name);
        originCodeTv = (TextView) itemView.findViewById(R.id.origin_code);
        departureTimeTv = (TextView) itemView.findViewById(R.id.departure_time);
        destinationCodeTv = (TextView) itemView.findViewById(R.id.destination_code);
        arrivalTimeTv = (TextView) itemView.findViewById(R.id.arrival_time);
        durationTv = (TextView) itemView.findViewById(R.id.duration);
        availabilitySeatTv = (TextView) itemView.findViewById(R.id.availability_seat);
        priceTv = (TextView) itemView.findViewById(R.id.price);
        detailScheduleTv = (TextView) itemView.findViewById(R.id.tap_for_details);
        this.listener = listener;
    }

    @Override
    public void bind(TrainScheduleViewModel trainScheduleViewModel) {
        setFlagTrainSchedule(trainScheduleViewModel.isFastestFlag(), trainScheduleViewModel.isCheapestFlag());
        trainNameTv.setText(trainScheduleViewModel.getTrainName());
        classNameTv.setText(trainScheduleViewModel.getDisplayClass() + " (" + trainScheduleViewModel.getClassTrain() + ")");
        originCodeTv.setText(trainScheduleViewModel.getOrigin());
        departureTimeTv.setText(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getDepartureTimestamp()));
        destinationCodeTv.setText(trainScheduleViewModel.getDestination());
        arrivalTimeTv.setText(TrainDateUtil.formatDate(TrainDateUtil.FORMAT_DATE_API,
                TrainDateUtil.FORMAT_TIME, trainScheduleViewModel.getArrivalTimestamp()));
        durationTv.setText(trainScheduleViewModel.getDisplayDuration());

        setAvailabilitySeat(trainScheduleViewModel.getAvailableSeat());

        priceTv.setText(trainScheduleViewModel.getDisplayAdultFare());
        detailScheduleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO go to detail schedule
            }
        });
    }

    private void setFlagTrainSchedule(boolean isFastestFlag, boolean isCheapestFlag) {
        clearHolder(flagItemLayout);
        flagItemLayout.setVisibility(isFastestFlag || isCheapestFlag ? View.VISIBLE : View.GONE);
        if (isCheapestFlag) {
            setContentFlag("TERMURAH", R.color.light_green, R.color.tkpd_main_green,
                    R.color.tkpd_main_green);
        }
        if (isFastestFlag) {
            setContentFlag("TERCEPAT", R.color.light_orange, R.color.orange,
                    R.color.deep_orange);
        }
    }

    private void setContentFlag(String title, int colorInside, int colorOutside, int colorText) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flag_layout, flagItemLayout, false);
        TextView flagItem = view.findViewById(R.id.flag_name);
        LinearLayout flagLayout = view.findViewById(R.id.layout_flag);

        flagItem.setText(title);
        flagItem.setTextColor(ContextCompat.getColor(context, colorText));
        flagLayout.setBackgroundResource(R.drawable.bg_round_corner_custom);

        LayerDrawable layerDrawable = (LayerDrawable) flagLayout.getBackground();
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.layer_inside);
        gradientDrawable.setColor(ContextCompat.getColor(context, colorInside));

        LayerDrawable layerDrawable1 = (LayerDrawable) flagLayout.getBackground();
        GradientDrawable gradientDrawable1 = (GradientDrawable) layerDrawable1.findDrawableByLayerId(R.id.layer_outside);
        gradientDrawable1.setStroke(1, ContextCompat.getColor(context, colorOutside));

        if (flagItemLayout.getChildCount() > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flagItem.getLayoutParams();
            layoutParams.setMargins(10, 0, 0, 0);
            flagLayout.setLayoutParams(layoutParams);
        }
        flagItemLayout.addView(view);
    }

    private void setAvailabilitySeat(int availableSeat) {
        if (availableSeat > 10) {
            availabilitySeatTv.setVisibility(View.INVISIBLE);
        } else {
            availabilitySeatTv.setVisibility(View.VISIBLE);
            if (availableSeat == 0) {
                availabilitySeatTv.setText("Penuh");
            } else {
                availabilitySeatTv.setText("Sisa " + availableSeat + " kursi");
            }
        }
    }

    private void clearHolder(LinearLayout linearLayout) {
        if (linearLayout.getChildCount() > 0) {
            linearLayout.removeAllViews();
        }
    }
}
