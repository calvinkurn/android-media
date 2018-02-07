package com.tokopedia.inbox.rescenter.detailv2.view.customview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;

/**
 * Created by hangnadi on 3/8/17.
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "deprecation"})
public class DetailView extends BaseView<DetailData, DetailResCenterFragmentView> {

    private TextView textComplaintDate;
    private View viewCustomer;
    private TextView textCustomerName;
    private View viewShop;
    private TextView textShopName;
    private TextView textAwbNumber;
    private TextView textInvoice;

    public DetailView(Context context) {
        super(context);
    }

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_rescenter_detail_view;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        textComplaintDate = (TextView) view.findViewById(R.id.tv_complaint_date);
        viewCustomer = view.findViewById(R.id.view_customer);
        textCustomerName = (TextView) view.findViewById(R.id.tv_people_name);
        viewShop = view.findViewById(R.id.view_shop);
        textShopName = (TextView) view.findViewById(R.id.tv_shop_name);
        textAwbNumber = (TextView) view.findViewById(R.id.tv_awb_number);
        textInvoice = (TextView) view.findViewById(R.id.tv_invoice);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull final DetailData data) {
        setVisibility(VISIBLE);
        boolean isSeller = SessionHandler.getLoginID(getContext())
                .equals(data.getBuyerID());
        textAwbNumber.setText(data.getAwbNumber());
        textComplaintDate.setText(data.getComplaintDateTimestamp());
        textCustomerName.setText(data.getBuyerName());
        textInvoice.setText(data.getInvoice());
        textShopName.setText(data.getShopName());
        viewShop.setVisibility(isSeller ? View.VISIBLE : View.GONE);
        viewCustomer.setVisibility(!isSeller ? View.VISIBLE : GONE);

        textCustomerName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionPeopleDetailClick(data.getBuyerID());
            }
        });
        textShopName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionShopDetailClick(data.getShopID());
            }
        });
        textInvoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionInvoiceClick(data.getInvoice(), data.getInvoiceUrl());
            }
        });
    }

    public void generateDeadlineBackgroundView(View v) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8});
        shape.setColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(shape);
        } else {
            v.setBackgroundDrawable(shape);
        }
    }
}

