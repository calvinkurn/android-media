package com.tokopedia.seller.purchase.detail.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tokopedia.seller.purchase.detail.activity.BookingCodeContract;

public class BookingCodePresenter implements BookingCodeContract.BookingPresenter {

    public static final int BARCODE_WIDTH = 256;
    public static final int BARCODE_HEIGHT = 61;
    private static final String API_BARCODE_TYPE128 = "128b";
    private static final String API_BARCODE_TYPE39 = "39";
    private static final String API_BARCODE_TYPE93 = "93";
    private static final String COPY_LABEL = "booking code";

    BookingCodeContract.BookingView mView;

    @Override
    public Bitmap generateBarcode(String code, String type) {
        Bitmap bitmap = null;
        BarcodeFormat format = null;
        switch (type) {
            case API_BARCODE_TYPE128:
                format = BarcodeFormat.CODE_128;
                break;
            case API_BARCODE_TYPE39:
                format = BarcodeFormat.CODE_39;
                break;
            case API_BARCODE_TYPE93:
                format = BarcodeFormat.CODE_93;
                break;
        }
        if (format != null) {
            try {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = multiFormatWriter
                        .encode(code, format, BARCODE_WIDTH, BARCODE_HEIGHT);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public void sendClipboard(ClipboardManager clipboardManager, String text) {
        clipboardManager.setPrimaryClip(
                ClipData.newPlainText(COPY_LABEL, text)
        );
        mView.showSuccessOnCopy();
    }

    @Override
    public void setView(BookingCodeContract.BookingView view) {
        mView = view;
    }

}
