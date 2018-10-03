package com.tokopedia.transaction.purchase.detail.activity;

import android.graphics.Bitmap;
import android.view.View;

public interface BookingCodeContract {

     interface BookingView {

        void initView(View view);

        void copyCode(View view);

        void showBarcode(Bitmap bitmap);

        void zoomBarcode();

    }

     interface BookingPresenter {

        Bitmap generateBarcode(String code, String type);

        void setView(BookingView view);

    }

}
