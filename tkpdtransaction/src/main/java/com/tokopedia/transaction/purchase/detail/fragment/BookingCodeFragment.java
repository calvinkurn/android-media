package com.tokopedia.transaction.purchase.detail.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.adapter.MessageAdapter;
import com.tokopedia.transaction.purchase.detail.activity.BookingCodeContract;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OnlineBooking;

public class BookingCodeFragment extends BaseDaggerFragment implements BookingCodeContract.BookingView {

    TextView bookingCode;
    RecyclerView recyclerView;
    ViewGroup copyLayout;
    OnlineBooking mData;

    public BookingCodeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mData = getArguments() != null ? getArguments().getParcelable("data") : null;
        initView(view);
    }

    @Override
    protected void initInjector() {

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

        if(mData != null) {
            bookingCode.setText(mData.getBookingCode());
            MessageAdapter adapter = new MessageAdapter(mData.getMessage());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void copyCode(View v) {
        TextView tvCode = v.findViewById(R.id.booking_code);
        String code = tvCode.getText().toString().trim();
        ClipboardManager clipboardManager =
                (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(
                ClipData.newPlainText("booking code", code)
        );
        NetworkErrorHelper.showSnackbar(
                getActivity(),
                getString(R.string.notification_awb_copied)
        );
    }

    @Override
    public void showBarcode(Bitmap bitmap) {

    }
}
