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
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ButtonData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.presenter.OrderDetailPresenter;

/**
 * Created by kris on 11/10/17. Tokopedia
 */

public class OrderDetailButtonLayout extends LinearLayout{

    private View mainView;

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
        mainView = inflater.inflate(R.layout.button_list_layout, this, true);
    }

    public void initButton(Context context,
                           OrderDetailPresenter presenter,
                           OrderDetailData data) {
        setBuyerOrderDetailOption(context, presenter, data);
        setSellerOrderDetailOption(context, presenter, data);
    }

    private void setBuyerOrderDetailOption(Context context,
                                           OrderDetailPresenter presenter,
                                           OrderDetailData data) {
        ButtonData buttonData = data.getButtonData();
        Button confirm = (Button) mainView.findViewById(R.id.confirm_button);
        confirm.setOnClickListener(onReceivePackage(context, presenter, data));
        switchVisibilty(confirm, (buttonData.getReceiveConfirmationVisibility()));

        Button finishOrder = (Button)mainView.findViewById(R.id.finish_button);
        finishOrder.setOnClickListener(onFinishButtonClicked(context, presenter, data));
        switchVisibilty(finishOrder, buttonData.getFinishOrderVisibility());

        Button track = (Button)mainView.findViewById(R.id.track_button);
        track.setOnClickListener(onTrackButtonClicked(context, presenter, data));
        switchVisibilty(track, buttonData.getTrackVisibility());

        Button askSellerButton = (Button)mainView.findViewById(R.id.ask_seller_button);
        askSellerButton.setOnClickListener(onAskSeller(context, presenter, data));
        switchVisibilty(askSellerButton, buttonData.getAskSellerVisibility());

        Button viewComplaint = (Button)mainView.findViewById(R.id.view_complaint_button);
        viewComplaint.setOnClickListener(onViewComplaintClicked(context, presenter, data));
        switchVisibilty(viewComplaint, buttonData.getViewComplaint());
    }

    private void setSellerOrderDetailOption(Context context, OrderDetailPresenter presenter, OrderDetailData data) {
        ButtonData buttonData = data.getButtonData();
        Button acceptOrder;
        acceptOrder = (Button) mainView.findViewById(R.id.accept_order);
        acceptOrder.setOnClickListener(onAcceptOrder(context));
        switchVisibilty(acceptOrder, buttonData.getAcceptOrderVisibility());

        Button changeCourier;
        changeCourier = (Button) mainView.findViewById(R.id.change_courier);
        changeCourier.setOnClickListener(onChangeCourier(context));
        switchVisibilty(changeCourier, buttonData.getChangeCourier());

        Button requestPickup;
        requestPickup = (Button) mainView.findViewById(R.id.request_pickup);
        requestPickup.setOnClickListener(onRequestPickup(context));
        switchVisibilty(requestPickup, buttonData.getRequestPickupVisibility());

        Button changeAwb;
        changeAwb = (Button) mainView.findViewById(R.id.change_awb) ;
        changeAwb.setOnClickListener(onChangeAwbClickedListener(context));
        switchVisibilty(changeAwb, buttonData.getChangeAwbVisibility());

        Button confirmShipping;
        confirmShipping = (Button) mainView.findViewById(R.id.confirm_shipping);
        confirmShipping.setOnClickListener(onConfirmShipping(context));
        switchVisibilty(confirmShipping, buttonData.getConfirmShippingVisibility());

        Button askBuyerButton;
        askBuyerButton = (Button) mainView.findViewById(R.id.ask_buyer_button);
        askBuyerButton.setOnClickListener(onAskBuyer(context));
        switchVisibilty(askBuyerButton, buttonData.getAskBuyerVisibility());

        Button rejectOrder;
        rejectOrder = (Button) mainView.findViewById(R.id.reject_order_button);
        rejectOrder.setOnClickListener(onRejectOrder(context));
        switchVisibilty(rejectOrder, buttonData.getRejectOrderVisibility());

        Button requestCancel;
        requestCancel = (Button) mainView.findViewById(R.id.request_cancel_button);
        requestCancel.setOnClickListener(onRequestCancellation(context, presenter, data));
        switchVisibilty(requestCancel, buttonData.getRequestCancelVisibility());

        Button complaint;
        complaint = (Button) mainView.findViewById(R.id.complaint);
        complaint.setOnClickListener(onComplaint(context));
        switchVisibilty(complaint, buttonData.getComplaintVisibility());

        Button cancelChance;
        cancelChance = (Button) mainView.findViewById(R.id.cancel_chance);
        cancelChance.setOnClickListener(onCancelSearch(context));
        switchVisibilty(cancelChance, buttonData.getCancelPeluangVisibility());
    }

    private View.OnClickListener onFinishButtonClicked(final Context context,
                                                       final OrderDetailPresenter presenter,
                                                       final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Finish Button", Toast.LENGTH_SHORT).show();
                presenter.processConfirmDeliver(context, data);
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
                presenter.processShowComplain(context, data);
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

    private View.OnClickListener onAcceptOrder(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Complaint", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onChangeCourier(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Complaint", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onRequestPickup(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onChangeAwbClickedListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onConfirmShipping(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
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

    private View.OnClickListener onRejectOrder(final Context context) {
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

    private void switchVisibilty(Button button, int responseVisibility) {
        if(responseVisibility == 1) button.setVisibility(VISIBLE);
        else button.setVisibility(GONE);
    }
}
