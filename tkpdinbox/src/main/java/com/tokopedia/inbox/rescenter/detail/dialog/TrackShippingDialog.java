package com.tokopedia.inbox.rescenter.detail.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.inbox.rescenter.detail.customadapter.TrackShippingAdapter;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/3/16.
 */
public class TrackShippingDialog {

    private final Context context;
    private final Dialog dialog;
    private int stepCreate;
    private TextView shippingRefNum;
    private ListView listviewTrackHistory;
    private TextView textViewStatus;
    private List<ResCenterTrackShipping.TrackHistory> trackHistoryList;
    private TextView labelReceiverName;
    private TrackShippingAdapter trackShippingAdapter;

    public TrackShippingDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.trackHistoryList = new ArrayList<>();
        stepCreate++;
    }

    public static TrackShippingDialog Builder(@NonNull Context context) {
        return new TrackShippingDialog(context);
    }

    public TrackShippingDialog initView() {
        if (stepCreate != 1) {
            throw new RuntimeException("Call this after builder()");
        } else {
            stepCreate++;
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tracking_result);
        shippingRefNum = (TextView) dialog.findViewById(R.id.ref_num);
        listviewTrackHistory = (ListView) dialog.findViewById(R.id.tracking_result);
        textViewStatus = (TextView) dialog.findViewById(R.id.status);
        labelReceiverName = (TextView) dialog.findViewById(R.id.receiver_name);

        trackShippingAdapter = new TrackShippingAdapter((Activity) context, trackHistoryList);
        listviewTrackHistory.setAdapter(trackShippingAdapter);

        return this;
    }

    public TrackShippingDialog initValue(ResCenterTrackShipping.TrackShipping trackShipping) {
        if (stepCreate != 2) {
            throw new RuntimeException("Call this after initView()");
        } else {
            stepCreate++;
        }

        shippingRefNum.setText(trackShipping.getShippingRefNum());

        try {
            if (!trackShipping.getReceiverName().equals("null")) {
                textViewStatus.setText(trackShipping.getReceiverName());
                textViewStatus.setVisibility(View.VISIBLE);
                labelReceiverName.setVisibility(View.VISIBLE);
            } else {
                textViewStatus.setVisibility(View.GONE);
                labelReceiverName.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            textViewStatus.setVisibility(View.GONE);
            labelReceiverName.setVisibility(View.GONE);
        }

        if (trackShipping.getTrackHistory() != null) {
            this.trackHistoryList.addAll(trackShipping.getTrackHistory());
            trackShippingAdapter.notifyDataSetChanged();
            listviewTrackHistory.setVisibility(View.VISIBLE);
        } else {
            listviewTrackHistory.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public TrackShippingDialog show() {
        if (stepCreate != 3) {
            throw new RuntimeException("Call this after initValue()");
        } else {
            stepCreate++;
        }
        dialog.show();
        return this;
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
