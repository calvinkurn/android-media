package com.tokopedia.transaction.purchase.detail.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenter;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public class OrderDetailButtonLayout extends LinearLayout{

    private Button acceptOrder;

    private Button changeCourier;

    private Button requestPickup;

    private Button confirm;

    private Button finishOrder;

    private Button changeAwb;

    private Button viewComplaint;

    private Button confirmShipping;

    private Button track;

    private Button askSellerButton;

    private Button askBuyerButton;

    private Button rejectOrder;

    private Button requestCancel;

    private Button complaint;

    private Button cancelChange;


    public OrderDetailButtonLayout(Context context) {
        super(context);
        initView(context);
    }

    public OrderDetailButtonLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OrderDetailButtonLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_list_layout, this, false);

    }

    public void initButton(Context context, OrderDetailPresenter presenter, OrderDetailData data) {

        setBuyerOrderDetailOption(context, presenter, data);

        Button acceptOrder;

        Button changeCourier;

        Button requestPickup;

        Button changeAwb;

        Button confirmShipping;

        Button askBuyerButton;

        Button rejectOrder;

        Button requestCancel;

        Button complaint;

        Button cancelChance;
    }

    private void setBuyerOrderDetailOption(Context context, OrderDetailPresenter presenter, OrderDetailData data) {
        Button confirm = (Button)findViewById(R.id.confirm_button);
        confirm.setOnClickListener(onReceivePackage(context, presenter, data));

        Button finishOrder = (Button)findViewById(R.id.finish_button);
        finishOrder.setOnClickListener(onFinishButtonClicked(context, presenter, data));

        Button track = (Button)findViewById(R.id.track_button);
        track.setOnClickListener(onTrackButtonClicked(context, presenter, data));

        Button askSellerButton = (Button)findViewById(R.id.ask_seller_button);
        askSellerButton.setOnClickListener(onAskSeller(context, presenter, data));

        Button viewComplaint = (Button)findViewById(R.id.view_complaint_button);
        viewComplaint.setOnClickListener(onViewComplaintClicked(context, presenter, data));
    }

    private View.OnClickListener onFinishButtonClicked(final Context context,
                                                       final OrderDetailPresenter presenter,
                                                       final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Finish Button", Toast.LENGTH_SHORT).show();
                presenter.processFinish(context, data);
            }
        };
    }

    private View.OnClickListener onViewComplaintClicked(final Context context,
                                                        final OrderDetailPresenter presenter,
                                                        final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Complaint Button Clicked", Toast.LENGTH_SHORT).show();
                presenter.processComplain(context, data);
            }
        };
    }

    private View.OnClickListener onReceivePackage(final Context context,
                                                  final OrderDetailPresenter presenter,
                                                  final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Receive Package", Toast.LENGTH_SHORT).show();
                presenter.processConfirmDeliver(context, data);
            }
        };
    }

    private View.OnClickListener onTrackButtonClicked(final Context context,
                                                      final OrderDetailPresenter presenter,
                                                      final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Receive Package", Toast.LENGTH_SHORT).show();
                presenter.processTrackOrder(context, data);
            }
        };
    }

    private View.OnClickListener onAskSeller(final Context context,
                                             final OrderDetailPresenter presenter,
                                             final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
                presenter.processAskSeller(context, data);
            }
        };
    }

    private View.OnClickListener onAskBuyer(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onRequestCancellation(final Context context,
                                                       final OrderDetailPresenter presenter,
                                                       final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Request Cancel", Toast.LENGTH_SHORT).show();
                presenter.processRequestCancelOrder(context, data);
            }
        };
    }

    private View.OnClickListener onComplaint(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Complaint", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onCancelSearch(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cancel Search", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onButtonError(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
