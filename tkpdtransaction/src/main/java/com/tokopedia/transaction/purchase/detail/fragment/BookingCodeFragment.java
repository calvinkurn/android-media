package com.tokopedia.transaction.purchase.detail.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.adapter.MessageAdapter;
import com.tokopedia.transaction.purchase.detail.activity.BookingCodeActivity;
import com.tokopedia.transaction.purchase.detail.activity.BookingCodeContract;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OnlineBooking;
import com.tokopedia.transaction.purchase.detail.presenter.BookingCodePresenter;
import com.tokopedia.transaction.purchase.utils.OrderDetailAnalytics;
import com.tokopedia.transaction.purchase.utils.OrderDetailConstant;
import com.tokopedia.transaction.router.ITransactionOrderDetailRouter;

public class BookingCodeFragment extends BaseDaggerFragment implements BookingCodeContract.BookingView {

    TextView bookingCode;
    ImageView barcodeImg;
    RecyclerView recyclerView;
    View filterView;
    ViewGroup copyLayout;
    CardView cardBarcode;
    OnlineBooking mData;
    BookingCodeContract.BookingPresenter mPresenter;

    OrderDetailAnalytics orderDetailAnalytics;

    public BookingCodeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orderDetailAnalytics = new OrderDetailAnalytics(
                (ITransactionOrderDetailRouter) getActivity().getApplication()
        );
        mData = getArguments() != null ? getArguments().getParcelable(BookingCodeActivity.JOB_CODE_EXTRA) : null;
        mPresenter = new BookingCodePresenter();
        mPresenter.setView(this);
        initView(view);
    }

    @Override
    protected void initInjector() {
        // todo: implement dagger on refactoring to new module
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void initView(View view) {
        bookingCode = view.findViewById(R.id.booking_code);
        copyLayout = view.findViewById(R.id.ll_code);
        copyLayout.setOnClickListener(this::copyCode);
        recyclerView = view.findViewById(R.id.rv_message);
        barcodeImg = view.findViewById(R.id.barcode_img);
        filterView = view.findViewById(R.id.filter_view);
        cardBarcode = view.findViewById(R.id.card_barcode);
        cardBarcode.setOnClickListener(view1 -> zoomBarcode());

        if(mData != null) {
            bookingCode.setText(mData.getBookingCode());
            MessageAdapter adapter = new MessageAdapter(mData.getMessage());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            showBarcode(mPresenter.generateBarcode(mData.getBookingCode(), mData.getBarcodeType()));
        }
    }

    @Override
    public void copyCode(View v) {
        TextView tvCode = v.findViewById(R.id.booking_code);
        String code = tvCode.getText().toString().trim();
        ClipboardManager clipboardManager =
                (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        mPresenter.sendClipboard(clipboardManager, code);
    }

    @Override
    public void showBarcode(Bitmap bitmap) {
        barcodeImg.setImageBitmap(bitmap);
    }

    @Override
    public void zoomBarcode() {
        orderDetailAnalytics.sendAnalyticsClickShipping(OrderDetailConstant.VALUE_CLICK_BARCODE,
                OrderDetailConstant.VALUE_EMPTY);
        filterView.setVisibility(View.VISIBLE);
        filterView.setOnClickListener(view -> view.setVisibility(View.GONE));
    }

    @Override
    public void showSuccessOnCopy() {
        orderDetailAnalytics.sendAnalyticsClickShipping(OrderDetailConstant.VALUE_CLICK_COPY_CODE,
                OrderDetailConstant.VALUE_EMPTY);
        NetworkErrorHelper.showGreenCloseSnackbar(getActivity(),
                getString(R.string.booking_code_copied_notif));
    }
}
