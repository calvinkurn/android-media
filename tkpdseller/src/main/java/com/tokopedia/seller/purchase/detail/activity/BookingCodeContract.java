package com.tokopedia.seller.purchase.detail.activity;

import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.view.View;

public interface BookingCodeContract {

     interface BookingView {

        void initView(View view);

        void copyCode(View view);

        void showBarcode(Bitmap bitmap);

        void zoomBarcode();

        void showSuccessOnCopy();

        void changeBarcodeSize(int dp);

    }

     interface BookingPresenter {

        Bitmap generateBarcode(String code, String type);

        void sendClipboard(ClipboardManager clipboardManager, String text);

        void setView(BookingView view);

    }

}
