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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;

/**
 * Created by yfsx on 12/12/17.
 */

public class CancelComplaintView extends BaseView<ButtonData, DetailResCenterFragmentView> {

    private TextView tvCancelComplaint;

    public CancelComplaintView(Context context) {
        super(context);
    }

    public CancelComplaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_cancel_complaint;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        tvCancelComplaint = (TextView) view.findViewById(R.id.tv_button);
    }

    @Override
    public void renderData(@NonNull ButtonData data) {
        setVisibility(VISIBLE);
        tvCancelComplaint.setText(data.getCancelLabel());
        tvCancelComplaint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionCancelResolutionClick();
            }
        });
    }
}
