package com.tokopedia.transaction.purchase.detail.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tokopedia.transaction.purchase.detail.activity.BookingCodeContract;

public class BookingCodePresenter implements BookingCodeContract.BookingPresenter {

    public static final int BARCODE_WIDTH = 256;
    public static final int BARCODE_HEIGHT = 61;

    BookingCodeContract.BookingView mView;

    @Override
    public Bitmap generateBarcode(String code, String type) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        BarcodeFormat format = null;
        switch (type) {
            case "128b":
                format = BarcodeFormat.CODE_128;
                break;
            case "39":
                format = BarcodeFormat.CODE_39;
                break;
            case "93":
                format = BarcodeFormat.CODE_93;
                break;
        }
        if (format != null) {
            try {
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
                ClipData.newPlainText("booking code", text)
        );
        mView.showSuccessOnCopy();
    }

    @Override
    public void setView(BookingCodeContract.BookingView view) {
        mView = view;
    }

}
