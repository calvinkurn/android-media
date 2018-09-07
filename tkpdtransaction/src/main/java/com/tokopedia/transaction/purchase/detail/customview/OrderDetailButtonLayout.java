package com.tokopedia.transaction.purchase.detail.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.constant.OrderDetailTypeDef;
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
        mainView = generateView(inflater);
    }

    private View generateView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.button_list_layout, this, true);
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

        Button finishOrder = mainView.findViewById(R.id.finish_button);
        finishOrder.setOnClickListener(onFinishButtonClicked(context, presenter, data));
        int visibilityButton;
        if (buttonData.getReceiveConfirmationVisibility() != OrderDetailTypeDef.HIDE_BUTTON) {
            visibilityButton = buttonData.getReceiveConfirmationVisibility();
        } else {
            visibilityButton = buttonData.getFinishOrderVisibility();
        }
        switchVisibilty(finishOrder, visibilityButton);

        Button track = mainView.findViewById(R.id.order_detail_track_button);
        track.setOnClickListener(onTrackButtonClicked(context, presenter, data));
        switchVisibilty(track, buttonData.getTrackVisibility());

        Button askSellerButton = mainView.findViewById(R.id.ask_seller_button);
        askSellerButton.setOnClickListener(onAskSeller(context, presenter, data));
        switchVisibilty(askSellerButton, buttonData.getAskSellerVisibility());

        Button complaint;
        complaint = mainView.findViewById(R.id.complaint);
        complaint.setOnClickListener(onComplaint(context, presenter, data));
        switchVisibilty(complaint, buttonData.getComplaintVisibility());

        Button requestCancel;
        requestCancel = mainView.findViewById(R.id.request_cancel_button);
        requestCancel.setOnClickListener(onRequestCancellation(context, presenter, data));
        switchVisibilty(requestCancel, buttonData.getRequestCancelVisibility());

        Button cancelChance;
        cancelChance = mainView.findViewById(R.id.cancel_chance);
        cancelChance.setOnClickListener(onCancelSearch(context, presenter, data));
        switchVisibilty(cancelChance, buttonData.getCancelPeluangVisibility());
    }

    private void setSellerOrderDetailOption(Context context, OrderDetailPresenter presenter, OrderDetailData data) {
        ButtonData buttonData = data.getButtonData();
        Button acceptOrder;
        acceptOrder = mainView.findViewById(R.id.accept_order);
        acceptOrder.setOnClickListener(onAcceptOrder(context, presenter, data));
        switchVisibilty(acceptOrder, buttonData.getAcceptOrderVisibility());

        Button acceptOrderPartial;
        acceptOrderPartial = mainView.findViewById(R.id.accept_order_patial);
        acceptOrderPartial.setOnClickListener(onAcceptOrderPartial(context, presenter, data));
        switchVisibilty(acceptOrderPartial, buttonData.getAcceptPartialOrderVisibility());

        Button confirmShipping;
        confirmShipping = mainView.findViewById(R.id.confirm_shipping);
        switchConfirmButtonMode(confirmShipping,
                buttonData.getConfirmShippingVisibility(),
                buttonData.getChangeCourier(),
                data,
                presenter,
                context);

        Button requestPickup;
        requestPickup = mainView.findViewById(R.id.request_pickup);
        requestPickup.setOnClickListener(onRequestPickup(context, presenter, data));
        switchVisibilty(requestPickup, buttonData.getRequestPickupVisibility());

        Button changeAwb;
        changeAwb = mainView.findViewById(R.id.change_awb);
        changeAwb.setOnClickListener(onChangeAwbClickedListener(context, presenter, data));
        switchVisibilty(changeAwb, buttonData.getChangeAwbVisibility());

        Button askBuyerButton;
        askBuyerButton = mainView.findViewById(R.id.ask_buyer_button);
        askBuyerButton.setOnClickListener(onAskBuyer(context, presenter, data));
        switchVisibilty(askBuyerButton, buttonData.getAskBuyerVisibility());

        Button rejectOrder;
        rejectOrder = mainView.findViewById(R.id.reject_order_button);
        rejectOrder.setOnClickListener(onRejectOrder(context, presenter, data));
        switchVisibilty(rejectOrder, buttonData.getRejectOrderVisibility());

        Button rejectShipment;
        rejectShipment = mainView.findViewById(R.id.reject_shipment_button);
        rejectShipment.setOnClickListener(onRejectShipment(context, presenter, data));
        switchVisibilty(rejectShipment, buttonData.getRejectShipmentVisibility());

        Button viewComplaint = mainView.findViewById(R.id.view_complaint_button);
        viewComplaint.setOnClickListener(onViewComplaintClicked(context, presenter, data));
        switchVisibilty(viewComplaint, buttonData.getViewComplaint());
    }

    private View.OnClickListener onFinishButtonClicked(final Context context,
                                                       final OrderDetailPresenter presenter,
                                                       final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                presenter.processShowComplain(context, data);
            }
        };
    }

    private View.OnClickListener onTrackButtonClicked(final Context context,
                                                      final OrderDetailPresenter presenter,
                                                      final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                presenter.processAskSeller(context, data);
            }
        };
    }

    private View.OnClickListener onAcceptOrder(final Context context,
                                               final OrderDetailPresenter presenter,
                                               final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processAcceptOrder(context, data);
            }
        };
    }

    private View.OnClickListener onAcceptOrderPartial(final Context context,
                                               final OrderDetailPresenter presenter,
                                               final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processAcceptPartialOrder(context, data);
            }
        };
    }

    private View.OnClickListener onChangeCourier(final Context context,
                                                 final OrderDetailPresenter presenter,
                                                 final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processChangeCourier(context, data);
            }
        };
    }

    private View.OnClickListener onRequestPickup(final Context context,
                                                 final OrderDetailPresenter presenter,
                                                 final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processRequestPickup(context, data);
            }
        };
    }

    private View.OnClickListener onChangeAwbClickedListener(final Context context,
                                                            final OrderDetailPresenter presenter,
                                                            final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processChangeAwb(context, data);
            }
        };
    }

    private View.OnClickListener onConfirmShipping(final Context context,
                                                   final OrderDetailPresenter presenter,
                                                   final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processSellerConfirmShipping(context, data);
            }
        };
    }

    private View.OnClickListener onAskBuyer(final Context context,
                                            final OrderDetailPresenter presenter,
                                            final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processAskBuyer(context, data);
            }
        };
    }

    private View.OnClickListener onRejectOrder(final Context context,
                                               final OrderDetailPresenter presenter,
                                               final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processRejectOrder(context, data);
            }
        };
    }

    private View.OnClickListener onRejectShipment(final Context context,
                                               final OrderDetailPresenter presenter,
                                               final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processRejectShipment(context, data);
            }
        };
    }

    private View.OnClickListener onRequestCancellation(final Context context,
                                                       final OrderDetailPresenter presenter,
                                                       final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processRequestCancelOrder(context, data);
            }
        };
    }

    private View.OnClickListener onComplaint(final Context context,
                                             final OrderDetailPresenter presenter,
                                             final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processComplaint(context, data);
            }
        };
    }

    private View.OnClickListener onCancelSearch(final Context context,
                                                final OrderDetailPresenter presenter,
                                                final OrderDetailData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processCancelSearch(context, data);
            }
        };
    }

    private void switchVisibilty(Button button, int responseVisibility) {
        if(responseVisibility == OrderDetailTypeDef.WHITE_BUTTON) {
            button.setVisibility(VISIBLE);
            button.setBackgroundResource(R.drawable.white_button_rounded);
            button.setTextColor(getResources().getColor(R.color.black));
        } else if(responseVisibility == OrderDetailTypeDef.GREEN_BUTTON) {
            button.setVisibility(VISIBLE);
            button.setBackgroundResource(R.drawable.green_button_rounded_unify);
            button.setTextColor(getResources().getColor(R.color.white));
        } else button.setVisibility(GONE);
    }

    private void switchConfirmButtonMode(Button button,
                                         int confirmButtonVisibility,
                                         int changeCourierButtonVisibility,
                                         OrderDetailData data,
                                         OrderDetailPresenter presenter,
                                         Context context) {
        if(confirmButtonVisibility == OrderDetailTypeDef.HIDE_BUTTON
                && changeCourierButtonVisibility == OrderDetailTypeDef.HIDE_BUTTON)
            button.setVisibility(GONE);
        else if(checkIfVisible(confirmButtonVisibility)
                && changeCourierButtonVisibility == OrderDetailTypeDef.HIDE_BUTTON) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.button_order_detail_confirm_shipping_alternative);
            button.setOnClickListener(onConfirmShipping(context, presenter, data));
        } else if(Integer.parseInt(data.getOrderCode()) >= 500 &&
                changeCourierButtonVisibility != OrderDetailTypeDef.HIDE_BUTTON) {
            button.setVisibility(GONE);
        }else {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(onChangeCourier(context, presenter, data));
        }
    }

    private boolean checkIfVisible(int visibility) {
        return visibility == OrderDetailTypeDef.WHITE_BUTTON
                || visibility == OrderDetailTypeDef.GREEN_BUTTON;
    }

}
