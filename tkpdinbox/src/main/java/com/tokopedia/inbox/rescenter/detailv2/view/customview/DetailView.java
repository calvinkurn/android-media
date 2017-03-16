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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.DetailData;

/**
 * Created by hangnadi on 3/8/17.
 */

public class DetailView extends BaseView<DetailData, DetailResCenterFragmentView> {

    private TextView textComplaintDate;
    private View viewCustomer;
    private TextView textCustomerName;
    private View viewShop;
    private TextView textShopName;
    private TextView textAwbNumber;
    private TextView textInvoice;
    private View viewBuyerResponseDeadline;
    private TextView textBuyerResponseDeadline;
    private View viewSellerResponseDeadline;
    private TextView textSellerResponseDeadline;

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
        viewBuyerResponseDeadline = view.findViewById(R.id.view_buyer_response_deadline);
        textBuyerResponseDeadline = (TextView) view.findViewById(R.id.tv_buyer_response_deadline);
        viewSellerResponseDeadline = view.findViewById(R.id.view_seller_response_deadline);
        textSellerResponseDeadline = (TextView) view.findViewById(R.id.tv_seller_response_deadline);
    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull final DetailData data) {
        setVisibility(VISIBLE);
        textAwbNumber.setText(data.getAwbNumber());
        textComplaintDate.setText(data.getComplaintDate());
        textCustomerName.setText(data.getBuyerName());
        textInvoice.setText(data.getInvoice());
        textShopName.setText(data.getShopName());
//        viewBuyerResponseDeadline.setVisibility(data.isBuyerDeadlineVisibility() ? VISIBLE : GONE);
        textBuyerResponseDeadline.setText(data.getResponseDeadline());
//        viewSellerResponseDeadline.setVisibility(data.isSellerDeadlineVisibility() ? VISIBLE : GONE);
        textSellerResponseDeadline.setText(data.getResponseDeadline());
        textCustomerName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionPeopleDetailClick(data.getBuyerID());
            }
        });
        textCustomerName.setOnClickListener(new OnClickListener() {
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
}

