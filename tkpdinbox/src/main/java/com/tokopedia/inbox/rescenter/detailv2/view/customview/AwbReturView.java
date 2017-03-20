package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AwbData;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AwbReturView extends BaseView<AwbData, DetailResCenterFragmentView> {

    private View actionTrack;
    private View actionMoreAwb;
    private TextView informationText;
    private TextView awbText;

    public AwbReturView(Context context) {
        super(context);
    }

    public AwbReturView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_awb_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionTrack = view.findViewById(R.id.action_track);
        actionMoreAwb = view.findViewById(R.id.action_awb_more);
        informationText = (TextView) view.findViewById(R.id.tv_last_awb_date);
        awbText = (TextView) view.findViewById(R.id.tv_awb_number);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull AwbData data) {
        setVisibility(VISIBLE);
        informationText.setText(generateInformationText(data));
        awbText.setText(data.getShipmentRef());
        actionTrack.setOnClickListener(new AwbViewOnClickListener(data.getShipmentRef(), data.getShipmentID()));
        actionMoreAwb.setOnClickListener(new AwbViewOnClickListener(data.getShipmentRef(), data.getShipmentID()));
    }

    private String generateInformationText(AwbData data) {
        return getContext().getString(R.string.template_awb_additional_text)
                .replace("X123", data.getAwbDate());
    }

    private class AwbViewOnClickListener implements OnClickListener {
        private final String shipmentID;
        private final String shipmentRef;

        public AwbViewOnClickListener(String shipmentRef, String shipmentID) {
            this.shipmentRef = shipmentRef;
            this.shipmentID = shipmentID;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.action_track) {
                listener.setOnActionTrackAwbClick(shipmentID, shipmentRef);
            } else if (view.getId() == R.id.action_awb_more) {
                listener.setOnActionAwbHistoryClick();
            }
        }
    }
}
