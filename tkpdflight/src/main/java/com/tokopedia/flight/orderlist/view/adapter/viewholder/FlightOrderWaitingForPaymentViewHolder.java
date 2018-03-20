package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.orderlist.data.cloud.entity.ManualTransferEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderWaitingForPaymentViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderWaitingForPaymentViewHolder extends FlightOrderBaseViewHolder<FlightOrderWaitingForPaymentViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_waiting_for_payment;
    private final FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener;

    private AppCompatTextView tvTitle;
    private AppCompatTextView tvOrderDate;
    private AppCompatTextView tvOrderId;
    private AppCompatTextView tvDepartureCity;
    private AppCompatTextView tvArrivalCity;
    private AppCompatTextView tvDetailOrder;
    private AppCompatTextView tvPaymentLabel;
    private AppCompatTextView tvPayment;
    private AppCompatTextView tvPaymentDescription;
    private AppCompatTextView tvPaymentDueDateLabel;
    private AppCompatTextView tvPaymentDueDate;
    private AppCompatTextView tvPaymentUniqueCodeLabel;
    private AppCompatTextView tvPaymentUniqueCode;
    private AppCompatTextView tvPaymentCostLabel;
    private AppCompatTextView tvPaymentCost;
    private LinearLayout paymentLayout;


    private FlightOrderWaitingForPaymentViewModel item;

    public FlightOrderWaitingForPaymentViewHolder(View itemView, FlightOrderAdapter.OnAdapterInteractionListener adapterInteractionListener) {
        super(itemView);
        this.adapterInteractionListener = adapterInteractionListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        tvTitle = (AppCompatTextView) view.findViewById(R.id.tv_title);
        tvOrderDate = (AppCompatTextView) view.findViewById(R.id.tv_order_date);
        tvOrderId = (AppCompatTextView) view.findViewById(R.id.tv_order_id);
        tvDepartureCity = (AppCompatTextView) view.findViewById(R.id.tv_departure_city);
        tvArrivalCity = (AppCompatTextView) view.findViewById(R.id.tv_arrival_city);
        tvDetailOrder = (AppCompatTextView) view.findViewById(R.id.tv_order_detail);
        tvDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailOptionClicked();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailOptionClicked();
            }
        });


        paymentLayout = (LinearLayout) view.findViewById(R.id.payment_info_layout);
        tvPaymentLabel = (AppCompatTextView) view.findViewById(R.id.tv_payment_label);
        tvPayment = (AppCompatTextView) view.findViewById(R.id.tv_payment);
        tvPaymentDescription = (AppCompatTextView) view.findViewById(R.id.tv_payment_description);
        tvPaymentDueDateLabel = (AppCompatTextView) view.findViewById(R.id.tv_payment_due_date_label);
        tvPaymentDueDate = (AppCompatTextView) view.findViewById(R.id.tv_payment_due_date);
        tvPaymentUniqueCodeLabel = (AppCompatTextView) view.findViewById(R.id.tv_payment_unique_code_label);
        tvPaymentUniqueCode = (AppCompatTextView) view.findViewById(R.id.tv_payment_unique_code);
        tvPaymentCostLabel = (AppCompatTextView) view.findViewById(R.id.tv_payment_cost_label);
        tvPaymentCost = (AppCompatTextView) view.findViewById(R.id.tv_payment_cost);
    }


    @Override
    public void bind(FlightOrderWaitingForPaymentViewModel element) {
        this.item = element;
        tvTitle.setText(element.getTitle());
        tvOrderDate.setText(FlightDateUtil.formatToUi(element.getCreateTime()));
        tvOrderId.setText(String.format("%s %s", itemView.getContext().getString(R.string.flight_order_order_id_prefix), element.getId()));
        if (element.getOrderJourney().size() > 0) {
            renderArrow(element.getOrderJourney());
            FlightOrderJourney orderJourney = element.getOrderJourney().get(0);
            tvDepartureCity.setText(getAirportTextForView(
                    orderJourney.getDepartureAiportId(),
                    orderJourney.getDepartureCityCode(),
                    orderJourney.getDepartureCity()));
            tvArrivalCity.setText(getAirportTextForView(
                    orderJourney.getArrivalAirportId(),
                    orderJourney.getArrivalCityCode(),
                    orderJourney.getArrivalCity()));
            renderDepartureSchedule(element.getOrderJourney());
        }

        renderPaymentInfo(element);
    }

    private void renderPaymentInfo(FlightOrderWaitingForPaymentViewModel element) {
        if (element.getPayment() != null && element.getPayment().getGatewayName().length() > 0) {
            paymentLayout.setVisibility(View.VISIBLE);
            if (element.getPayment().getManualTransfer() != null && element.getPayment().getManualTransfer().getAccountBankName().length() > 0) {
                tvPaymentLabel.setText(R.string.flight_order_payment_manual_label);
                tvPaymentUniqueCodeLabel.setVisibility(View.GONE);
                tvPaymentUniqueCode.setVisibility(View.GONE);
                tvPaymentDescription.setVisibility(View.VISIBLE);
                tvPaymentDescription.setText(renderDescriptionText(element.getPayment().getManualTransfer()));
                tvPayment.setText(element.getPayment().getManualTransfer().getAccountBankName());
                tvPaymentCostLabel.setVisibility(View.VISIBLE);
                tvPaymentCost.setVisibility(View.VISIBLE);
                tvPaymentCost.setText(element.getPayment().getManualTransfer().getTotal());
            } else {
                tvPayment.setText(element.getPayment().getGatewayName());
                tvPaymentLabel.setText(R.string.flight_order_payment_label);
                tvPaymentUniqueCodeLabel.setVisibility(View.VISIBLE);
                tvPaymentUniqueCode.setVisibility(View.VISIBLE);
                tvPaymentDescription.setVisibility(View.GONE);
                if (element.getPayment().getTotalAmount() > 0){
                    tvPaymentCostLabel.setVisibility(View.VISIBLE);
                    tvPaymentCost.setVisibility(View.VISIBLE);
                    tvPaymentCost.setText(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(element.getPayment().getTotalAmount()));
                } else {
                    tvPaymentCostLabel.setVisibility(View.GONE);
                    tvPaymentCost.setVisibility(View.GONE);
                }
                tvPaymentUniqueCode.setText(element.getPayment().getTransactionCode());
            }

            tvPaymentDueDate.setText(FlightDateUtil.formatDateByUsersTimezone(FlightDateUtil.FORMAT_DATE_API, FlightDateUtil.DEFAULT_VIEW_TIME_FORMAT, element.getPayment().getExpireOn()));

        } else {
            paymentLayout.setVisibility(View.GONE);
        }
    }

    private String renderDescriptionText(ManualTransferEntity manualTransfer) {
        String newLine = "\n";
        StringBuilder result = new StringBuilder();
        result.append(itemView.getContext().getString(R.string.flight_order_a_n_prefix) + " " + manualTransfer.getAccountName() + newLine);
        result.append(itemView.getContext().getString(R.string.flight_order_branch_prefix) + " " + manualTransfer.getAccountBranch() + newLine);
        result.append(manualTransfer.getAccountNo());
        return result.toString();
    }

    @Override
    protected void onHelpOptionClicked() {
        adapterInteractionListener.onHelpOptionClicked(item.getId(), item.getStatus());
    }

    @Override
    protected void onDetailOptionClicked() {
        if (item.getOrderJourney().size() == 1) {
            FlightOrderDetailPassData passData = new FlightOrderDetailPassData();
            passData.setOrderId(item.getId());
            FlightOrderJourney orderJourney = item.getOrderJourney().get(0);
            passData.setDepartureAiportId(orderJourney.getDepartureAiportId());
            passData.setDepartureCity(orderJourney.getDepartureCity());
            passData.setDepartureTime(orderJourney.getDepartureTime());
            passData.setArrivalAirportId(orderJourney.getArrivalAirportId());
            passData.setArrivalCity(orderJourney.getArrivalCity());
            passData.setArrivalTime(orderJourney.getArrivalTime());
            passData.setStatus(item.getStatus());
            adapterInteractionListener.onDetailOrderClicked(passData);
        } else {
            adapterInteractionListener.onDetailOrderClicked(item.getId());
        }
    }
}