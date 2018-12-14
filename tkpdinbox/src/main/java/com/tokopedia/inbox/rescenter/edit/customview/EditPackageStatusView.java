package com.tokopedia.inbox.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.listener.BuyerEditResCenterListener;

import butterknife.BindView;

/**
 * Created on 8/25/16.
 */
public class EditPackageStatusView extends BaseView<DetailResCenterData, BuyerEditResCenterListener> {

    @BindView(R2.id.radioGroup)
    RadioGroup radioGroup;

    public EditPackageStatusView(Context context) {
        super(context);
    }

    public EditPackageStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(BuyerEditResCenterListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_form_package_status;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull DetailResCenterData data) {
        radioGroup.setOnCheckedChangeListener(new OnRadioChangeListener());
        radioGroup.check(isReceived(data) ? R.id.radio_flag_received : R.id.radio_flag_not_received);
        setVisibility(isReceived(data) ? GONE : VISIBLE);
    }

    private boolean isReceived(DetailResCenterData data) {
        return data.getDetail().getResolutionLast().getLastFlagReceived() == 1;
    }

    public int getPackageState() {
        return radioGroup.getCheckedRadioButtonId() == R.id.radio_flag_received ? 1 : 0;
    }

    private class OnRadioChangeListener
            implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_flag_received) {
                listener.getPresenter().setOnRadioPackageStatus(true);
            } else {
                listener.getPresenter().setOnRadioPackageStatus(false);
            }
        }
    }
}
