package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.presenter.FlightDetailOrderContract;
import com.tokopedia.flight.detail.presenter.FlightDetailOrderPresenter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderAdapter;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapter;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPriceAdapter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderFragment extends BaseDaggerFragment implements FlightDetailOrderContract.View {

    private TextView orderId;
    private ImageView copyOrderId;
    private View containerDownloadEticket;
    private TextView orderStatus;
    private TextView transactionDate;
    private TextView refundDate;
    private TextView refundStatus;
    private RecyclerView recyclerViewFlight;
    private RecyclerView recyclerViewPassenger;
    private RecyclerView recyclerViewPrice;
    private View containerDownloadInvoice;
    private TextView totalPrice;
    private TextView orderHelp;
    private Button buttonCancelTicket;
    private Button buttonRescheduleTicket;

    private FlightDetailOrderAdapter flightDetailOrderAdapter;
    private FlightBookingReviewPassengerAdapter flightBookingReviewPassengerAdapter;
    private FlightBookingReviewPriceAdapter flightBookingReviewPriceAdapter;

    @Inject
    FlightDetailOrderPresenter flightDetailOrderPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightOrderComponent.class)
                .inject(this);
        flightDetailOrderPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_detail_order, container, false);
        orderId = view.findViewById(R.id.order_id_detail);
        copyOrderId = view.findViewById(R.id.copy_order_id);
        containerDownloadEticket = view.findViewById(R.id.container_download_eticket);
        orderStatus = view.findViewById(R.id.status_ticket);
        transactionDate = view.findViewById(R.id.transaction_date);
        refundDate = view.findViewById(R.id.refund_date);
        refundStatus = view.findViewById(R.id.status_refund);
        recyclerViewFlight = view.findViewById(R.id.recycler_view_flight);
        recyclerViewPassenger = view.findViewById(R.id.recycler_view_data_passenger);
        recyclerViewPrice = view.findViewById(R.id.recycler_view_detail_price);
        containerDownloadInvoice = view.findViewById(R.id.container_download_invoice);
        totalPrice = view.findViewById(R.id.total_price);
        orderHelp = view.findViewById(R.id.help);
        buttonCancelTicket = view.findViewById(R.id.button_cancel);
        buttonRescheduleTicket = view.findViewById(R.id.button_reschedule);

        flightDetailOrderAdapter = new FlightDetailOrderAdapter();
        flightBookingReviewPassengerAdapter = new FlightBookingReviewPassengerAdapter(getActivity());
        flightBookingReviewPriceAdapter = new FlightBookingReviewPriceAdapter(getActivity());
        return view;
    }

    public static Fragment createInstance() {
        return new FlightDetailOrderFragment();
    }
}
