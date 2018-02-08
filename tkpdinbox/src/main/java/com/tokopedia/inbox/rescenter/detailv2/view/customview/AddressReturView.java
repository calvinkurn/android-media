package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AddressReturData;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AddressReturView extends BaseView<AddressReturData, DetailResCenterFragmentView> {

    private TextView addressText;
    private TextView addressReturDate;
    private View actionMoreAddress;
    private View actionEdit;

    public AddressReturView(Context context) {
        super(context);
    }

    public AddressReturView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_address_retur_view;
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        actionEdit = view.findViewById(R.id.action_edit);
        actionMoreAddress = view.findViewById(R.id.action_address_more);
        addressReturDate = view.findViewById(R.id.tv_address_retur_date);
        addressText = view.findViewById(R.id.tv_address);
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull AddressReturData data) {
        setVisibility(VISIBLE);
        addressReturDate.setText(generateInformationText(data));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            addressText.setText(Html.fromHtml(data.getAddressText(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            addressText.setText(Html.fromHtml(data.getAddressText()));
        }
        actionEdit.setVisibility(listener.isSeller() ? VISIBLE : GONE);
        actionEdit.setOnClickListener(new AddressReturViewOnClickListener());
        actionMoreAddress.setOnClickListener(new AddressReturViewOnClickListener());
    }

    private String generateInformationText(AddressReturData data) {
        return getContext().getString(R.string.template_awb_additional_text,
                data.getAddressReturDateTimestamp());
    }

    private class AddressReturViewOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.action_edit) {
                listener.setOnActionEditAddressClick();
            } else if (view.getId() == R.id.action_address_more) {
                listener.setOnActionAddressHistoryClick();
            }
        }
    }
}
